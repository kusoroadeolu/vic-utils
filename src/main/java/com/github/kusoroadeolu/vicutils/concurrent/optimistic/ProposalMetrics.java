package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

public interface ProposalMetrics {
    long acceptedCount();
    long rejectedCount();
    double rejectionRate();
}
