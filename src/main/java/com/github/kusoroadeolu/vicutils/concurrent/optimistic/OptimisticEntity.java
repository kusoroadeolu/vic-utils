package com.github.kusoroadeolu.vicutils.concurrent.optimistic;


import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/** This class is meant to model a loosy STM like + actor model.
 * </br> Failed proposals will be dropped automatically. Batch failed proposals are dropped automatically too. No retries
 * </br> The core invariant is if a proposal is operating with stale value, the proposal is stale automatically, batch or not. Though this could lead to high drop rates lol
 * </br> I'm wondering what changes I could make here to make this better. But this is a solid start
 * */
class OptimisticEntity<E> implements Entity<E>, ProposalMetrics<E>{
    private volatile E state;
    private final ArrayBlockingQueue<Proposable<E>> queue = new ArrayBlockingQueue<>(Short.MAX_VALUE);
    private final List<List<Proposal<E, ?>>> rejectedProposals = new ArrayList<>();
    private volatile boolean isRunning = true; //volatile here for visibility guarantees
    private volatile long rejectedCount = 0;
    private volatile long versionNo = 0;
    private Map<Long, E> versions = new HashMap<>();

    private volatile long proposalsSubmitted = 0;

     OptimisticEntity(E e){
        state = e;
        this.versions.put(versionNo, state);
        this.start();
     }


    public <T>void propose(Proposal<E, T> proposal){
        queue.add(proposal);
    }

    public <T> void propose(BatchProposal<E, T> batchProposal){
        queue.add(batchProposal);
    }

     void start(){
        Thread.startVirtualThread(() -> {
            while (isRunning){
                Proposable<E> proposable = queue.poll();
                if (proposable != null){
                    proposalsSubmitted++;
                    List<Proposal<E, ?>> proposals = switch (proposable){
                        case Proposal<E, ?> p -> List.of(p);
                        case BatchProposal<E, ?> bp ->  castProposals(bp.proposals()); //The issue is here
                    };

                    this.processProposals(proposals, proposable.onSuccess(), proposable.onReject());
                }
            }
        });
     }

    @SuppressWarnings("unchecked")
    private <T> List<Proposal<E, ?>> castProposals(List<Proposal<E, T>> proposals) {
        return (List<Proposal<E, ?>>)(List<?>) proposals;
    }


    @SuppressWarnings("unchecked")
    private <T>void processProposals(List<Proposal<E, ?>> proposals, Runnable onSuccess, Runnable onReject){
        int count = 0;
        for (Proposal<E, ?> proposal : proposals) {
            T currentVal = (T) proposal.getter().apply(state);
            if (!Objects.equals(currentVal, proposal.seenValue())) break;
            count++;
        }

        if (count != proposals.size()) {
            rejectedProposals.add(proposals);
            rejectedCount++;
            tryRun(onReject);
            return;
        }

        for (Proposal<E, ?> proposal : proposals) {
            state = applyProposal(proposal);
        }

        versions.put(++versionNo, state);
        tryRun(onSuccess);
    }

    private void tryRun(Runnable runnable){
         if (runnable != null) runnable.run();
    }
    public E snapshot(){
         return state;
    }

    public void stop() {
        this.isRunning = false;
        this.queue.clear();
        this.versions.clear();
    }

    public List<List<Proposal<E, ?>>> rejectedProposals() {
        return Collections.unmodifiableList(this.rejectedProposals);
    }

    public long currentVersionNo() {
        return versionNo;
    }

    public long rejectedCount() {
        return rejectedCount;
    }

    public double rejectionRate() {
        return (double) (rejectedCount / proposalsSubmitted);
    }

    public E snapshotAt(long version) {
        return versions.get(version);
    }


    private <T> E applyProposal(Proposal<E, T> proposal) {
        return proposal.setter().apply(state, proposal.proposedValue());
    }
}
