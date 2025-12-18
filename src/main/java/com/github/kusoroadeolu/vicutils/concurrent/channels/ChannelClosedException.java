package com.github.kusoroadeolu.vicutils.concurrent.channels;

public class ChannelClosedException extends RuntimeException {
    public ChannelClosedException(String message) {
        super(message);
    }
}
