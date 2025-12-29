package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

public sealed interface Proposable<E> permits BatchProposal, Proposal {
    Runnable onSuccess();
    Runnable onReject();
}
