package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

//Where e is the entity and T is the type to be updated
public final class Proposal<E, T> implements Proposable<E>{
    private int versionNo;
    private T proposedValue;
    private BiFunction<E, T, E> setter;
    private Runnable onSuccess;
    private Runnable onReject;

    public Proposal(){

    }

    private Proposal(ProposalBuilder<E, T> builder){
        setter = builder.setter;
        onReject = builder.onReject;
        onSuccess = builder.onSuccess;
        versionNo = builder.versionNo;
        proposedValue = builder.proposedValue;
    }

    public ProposalBuilder<E, T> builder(){
        return new ProposalBuilder<>();
    }

    public T proposedValue(){
        return proposedValue;
    }

    public int versionNo(){
        return this.versionNo;
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
        private BiFunction<E, T, E> setter;
        private int versionNo = -1;
        private T proposedValue;
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

        public ProposalBuilder<E, T> versionNo(int versionNo){
            if (versionNo < 0) throw new IllegalArgumentException("versionNo < 0");
            this.versionNo = versionNo;
            return this;
        }

        public ProposalBuilder<E, T> proposedValue(T proposedValue) {
            requireNonNull(proposedValue);
            this.proposedValue = proposedValue;
            return this;
        }


        public Proposal<E, T> build(){
            return new Proposal<>(this);
        }
    }
}
