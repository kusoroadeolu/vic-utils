package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.Optional;

public class RendezvousChannel<T> implements Channel<T>{

    private T t;

    @Override
    public SendChannel<T> makeSendChannel() {
        return null;
    }

    @Override
    public ReceiveChannel<T> makeReceiveChannel() {
        return null;
    }

    @Override
    public Optional<T> receive() {
        return Optional.empty();
    }

    @Override
    public Optional<T> tryReceive() {
        return Optional.empty();
    }

    @Override
    public void send(T val) {

    }

    @Override
    public boolean trySend(T val) {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public int capacity() {
        return 0;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public boolean ok() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void make() {

    }
}
