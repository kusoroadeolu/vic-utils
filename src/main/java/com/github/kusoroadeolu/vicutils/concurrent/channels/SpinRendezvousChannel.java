package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This implementation invariants are the same as that as {@linkplain RendezvousChannel}
 * However the technical invariants involves using CAS spin semantics for synchronization rather than traditional locks
 * */
public class SpinRendezvousChannel<T> implements Channel<T>{

    private final AtomicReference<ChannelState> state;
    private final AtomicReference<T> ref;


    public SpinRendezvousChannel() {
        this.state = new AtomicReference<>(ChannelState.NIL);
        this.ref = new AtomicReference<>(null);
    }

    @Override
    public void make() {
        if (!state.compareAndSet(ChannelState.NIL, ChannelState.OPEN)){
            if (this.isClosed()) throw new ChannelClosedException("Channel is already closed");
        }
    }

    @Override
    public SendChannel<T> makeSendChannel() {
        this.make();
        return this;
    }

    @Override
    public ReceiveChannel<T> makeReceiveChannel() {
        this.make();
        return this;
    }

    @Override
    public Optional<T> tryReceive() {
        if (this.isNil()) throw new ChannelNilException("Channel is Nil");
        else if (this.isClosed() && this.isEmpty()) return Optional.empty();
        else return Optional.ofNullable(ref.get());
    }

    @Override
    public Optional<T> receive() {
        if (this.isNil()) throw new ChannelNilException("Channel is Nil");
        else if (this.isClosed() && this.isEmpty()) return Optional.empty();
        T val ;

        //While this is not closed
        // If the val is not null && val is equals to what we have in the reference, return val
        // Else keep waiting
        while (!this.isClosed()){
            val = ref.get();
            if (val != null && ref.compareAndSet(val, null)) {
                return Optional.of(val);
            } else {
                Thread.onSpinWait(); // Spin while empty
            }

        }

        ref.set(null);
        return Optional.empty();
    }

    //
    @Override
    public void send(T val) {
        Objects.requireNonNull(val);
        this.validateOnSend();
        //First wait loop i.e. is full condition
        while (!ref.compareAndSet(null, val)){
            if (this.isClosed()) {
                throw new ChannelClosedException("Channel is closed");
            }
            Thread.onSpinWait();
        }

        //2nd wait loop. i.e. item consumed condition
        while (ref.get() == val){
            if (this.isClosed()) {
                throw new ChannelClosedException("Channel is closed");
            }
            Thread.onSpinWait();
        }
    }

    @Override
    public boolean trySend(T val) {
        Objects.requireNonNull(val);
        this.validateOnSend();
        return ref.compareAndSet(null, val);
    }

    @Override
    public void close() {
        if (!this.state.compareAndSet(ChannelState.OPEN, ChannelState.CLOSED)){
            if (this.isClosed()){
                throw new ChannelClosedException("Channel is already closed");
            }
            else if (this.isNil()) throw new ChannelNilException("Channel is Nil");
        }
    }

    @Override
    public int capacity() {
        if (this.isNil()) return 0;
        else return 1;
    }

    @Override
    public int length() {
        return isEmpty() ? 0 : 1;
    }

    @Override
    public boolean ok() {
        return !this.isClosed() && !this.isNil();
    }

    @Override
    public boolean isEmpty() {
        return ref.get() == null;
    }

    void validateOnSend(){
        if (this.isNil()) throw new ChannelNilException("Channel is Nil");
        else if (this.isClosed()) throw new ChannelClosedException("Channel is Closed");
    }

    boolean isClosed(){
        return state.get() == ChannelState.CLOSED;
    }

    boolean isNil(){
        return state.get() == ChannelState.NIL;
    }

    boolean isOpen(){
        return state.get() == ChannelState.OPEN;
    }

    private enum ChannelState{
        NIL, OPEN, CLOSED;
    }
}
