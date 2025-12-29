package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

import java.util.List;

public interface ProposalMetrics<E> {
    long currentVersionNo();
    long rejectedCount();
    double rejectionRate();
    List<List<Proposal<E, ?>>> rejectedProposals();

}
