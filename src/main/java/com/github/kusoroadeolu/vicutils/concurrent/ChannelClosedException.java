package com.github.kusoroadeolu.vicutils.concurrent;

public class ChannelClosedException extends RuntimeException {
    public ChannelClosedException(String message) {
        super(message);
    }
}
