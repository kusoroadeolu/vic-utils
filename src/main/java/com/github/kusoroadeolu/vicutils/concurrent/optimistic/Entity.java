package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

import java.util.List;

public interface Entity<E> {

    <T>void propose(Proposal<E, T> proposal);

    <T> void propose(BatchProposal<E, T> proposals);


    void stop();

    List<List<Proposal<E, ?>>> rejectedProposals();

    E snapshot();
}
