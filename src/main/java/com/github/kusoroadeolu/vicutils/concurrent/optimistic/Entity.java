package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

import java.util.List;

public interface Entity<E> {

    <T>void propose(Proposal<E, T> proposal);

    <T> void propose(List<Proposal<E, T>> proposals);

    void stop();

    List<List<Proposal<E, ?>>> rejectedProposals();
}
