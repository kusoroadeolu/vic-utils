package com.github.kusoroadeolu.vicutils.concurrent.optimistic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

class OptimisticEntity<E> implements Entity<E>{
    private E state;
    private final ArrayBlockingQueue<List<Proposal<E, ?>>> queue = new ArrayBlockingQueue<>(Short.MAX_VALUE);
    private final List<List<Proposal<E, ?>>> droppedProposals = new ArrayList<>();
    private boolean isRunning = true;
    private final Object lock = new Object();

    public OptimisticEntity(E e){
        state = e;
        this.start();
    }

    @Override
    public <T>void propose(Proposal<E, T> proposal){
        queue.add(List.of(proposal));
    }

    @Override
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
    }

    public List<List<Proposal<E, ?>>> rejectedProposals() {
        return Collections.unmodifiableList(this.droppedProposals);
    }

    private <T> E applyProposal(Proposal<E, T> proposal) {
        return proposal.getSetter().apply(state, proposal.getProposedValue());
    }
}
