package com.github.kusoroadeolu.vicutils.concurrent.actors;

import com.github.kusoroadeolu.vicutils.concurrent.channels.BufferedChannel;
import com.github.kusoroadeolu.vicutils.concurrent.channels.Channel;

import java.util.Optional;

public class MailBox<E>{
    private final Channel<E> mailBox;

    public MailBox() {
        this.mailBox = new BufferedChannel<>(Integer.MAX_VALUE);
        this.mailBox.make();
    }

    public Optional<E> receive() {
        return this.mailBox.receive();
    }

    public void send(E val) {
        this.mailBox.send(val);
    }

    public void close() {
        this.mailBox.close();
    }
}
