package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

//Where e is the entity and T is the type to be updated
public final class Proposal<E, T> implements Proposable<E>{
    private Function<E, T> getter;
    private T seenValue;
    private T proposedValue;
    private BiFunction<E, T, E> setter;
    private Runnable onSuccess;
    private Runnable onReject;

    public Proposal(){

    }

    private Proposal(ProposalBuilder<E, T> builder){
        getter = builder.getter;
        seenValue = builder.seenValue;
        proposedValue = builder.proposedValue;
        setter = builder.setter;
        onReject = builder.onReject;
        onSuccess = builder.onSuccess;
    }

    public ProposalBuilder<E, T> builder(){
        return new ProposalBuilder<>();
    }

    public Function<E, T> getter() {
        return getter;
    }

    public T seenValue() {
        return seenValue;
    }

    public T proposedValue() {
        return proposedValue;
    }

    public BiFunction<E, T, E> setter() {
        return setter;
    }

    public Runnable onSuccess() {
        return onSuccess;
    }

    public Runnable onReject() {
        return onReject;
    }

    public static class ProposalBuilder<E, T>{
        private Function<E, T> getter;
        private T seenValue;
        private T proposedValue;
        private BiFunction<E, T, E> setter;
        private Runnable onSuccess;
        private Runnable onReject;

        private ProposalBuilder(){

        }

        public ProposalBuilder<E, T> onReject(Runnable onReject) {
            this.onReject = onReject;
            return this;
        }

        public ProposalBuilder<E, T> onSuccess(Runnable onSuccess) {
            this.onSuccess = onSuccess;
            return this;
        }

        public ProposalBuilder<E, T> setter(BiFunction<E, T, E> setter) {
            requireNonNull(setter);
            this.setter = setter;
            return this;
        }

        public ProposalBuilder<E, T> proposedValue(T proposedValue) {
            requireNonNull(proposedValue);
            this.proposedValue = proposedValue;
            return this;
        }

        public ProposalBuilder<E, T> seenValue(T seenValue) {
            requireNonNull(seenValue);
            this.seenValue = seenValue;
            return this;
        }

        public ProposalBuilder<E, T> getter(Function<E, T> getter) {
            requireNonNull(getter);
            this.getter = getter;
            return this;
        }

        public Proposal<E, T> build(){
            return new Proposal<>(this);
        }
    }
}
