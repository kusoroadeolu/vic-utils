package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

public interface Entity<E> extends ProposalMetrics<E>{

    <T>void propose(Proposal<E, T> proposal);

    <T> void propose(BatchProposal<E, T> proposals);

    void stop();

    E snapshotAt(long version);
}
