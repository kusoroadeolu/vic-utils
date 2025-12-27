package com.github.kusoroadeolu.vicutils.concurrent.actors;

import com.github.kusoroadeolu.vicutils.concurrent.channels.BufferedChannel;
import com.github.kusoroadeolu.vicutils.concurrent.channels.Channel;

import java.util.Optional;

public class MailBox<T>{
    private final Channel<T> mailBox;
    private final static int MAX_SIZE = 10_000;

    public MailBox() {
        this.mailBox = new BufferedChannel<>(MAX_SIZE);
        this.mailBox.make();
    }

    public Optional<T> receive() {
        return this.mailBox.receive();
    }

    public boolean send(T message) {
        this.mailBox.trySend(message);
    }

    public void close() {
        this.mailBox.close();
    }
}
