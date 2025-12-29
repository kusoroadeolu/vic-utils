package com.github.kusoroadeolu.vicutils.concurrent.optimistic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

/** This class is meant to model a loosy STM like + actor model.
 * </br> Failed proposals will be dropped automatically. Batch failed proposals are dropped automatically too. No retries
 * </br> The core invariant is if a proposal is operating with stale value, the proposal is stale automatically, batch or not. Though this could lead to high drop rates lol
 * </br> I'm wondering what changes I could make here to make this better. But this is a solid start
 * */
class OptimisticEntity<E> implements Entity<E>{
    private E state;
    private final ArrayBlockingQueue<List<Proposal<E, ?>>> queue = new ArrayBlockingQueue<>(Short.MAX_VALUE);
    private final List<List<Proposal<E, ?>>> droppedProposals = new ArrayList<>();
    private volatile boolean isRunning = true; //volatile here for visibility guarantees
    private final Object lock = new Object();

     OptimisticEntity(E e){
        state = e;
        this.start();
     }


    public <T>void propose(Proposal<E, T> proposal){
        queue.add(List.of(proposal));
    }

    public <T> void propose(List<Proposal<E, T>> proposal){
        queue.add(List.copyOf(proposal));
    }

     void start(){
        Thread.startVirtualThread(() -> {
            while (isRunning){
                List<Proposal<E, ?>> proposals = queue.poll();
                if (proposals != null && !proposals.isEmpty()){
                    this.processProposals(proposals);
                }
            }
        });
     }

    @SuppressWarnings("unchecked")
    private <T>void processProposals(List<Proposal<E, ?>> proposals){
        int count = 0;
        for (Proposal<E, ?> proposal : proposals) {
            T currentVal = (T) proposal.getGetter().apply(state);
            if (!Objects.equals(currentVal, proposal.getSeenValue())) break;
            count++;
        }

        if (count != proposals.size()) {
            droppedProposals.add(proposals);
            return;
        }

        for (Proposal<E, ?> proposal : proposals) {
            synchronized (lock){
                state = applyProposal(proposal);
            }//This is here so that any writes that happen to the objects internals are flushed to main memory.
            // volatile as a store-store barrier won't work here
        }
    }

    public void stop() {
        this.isRunning = false;
        this.queue.clear();
    }

    public List<List<Proposal<E, ?>>> rejectedProposals() {
        return Collections.unmodifiableList(this.droppedProposals);
    }

    private <T> E applyProposal(Proposal<E, T> proposal) {
        return proposal.getSetter().apply(state, proposal.getProposedValue());
    }
}
