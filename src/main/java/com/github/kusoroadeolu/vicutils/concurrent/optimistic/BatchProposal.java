package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

import java.util.List;

public record BatchProposal<E, T>(List<Proposal<E, T>> proposals, Runnable onSuccess, Runnable onReject) implements Proposable<E> {
}

