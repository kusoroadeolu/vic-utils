package com.github.kusoroadeolu.vicutils.concurrent.actors;

import com.github.kusoroadeolu.vicutils.concurrent.channels.BufferedChannel;
import com.github.kusoroadeolu.vicutils.concurrent.channels.Channel;

import java.util.Optional;

public class MailBox<T>{
    private final Channel<T> mailBox;

    public MailBox() {
        this.mailBox = new BufferedChannel<>(Integer.MAX_VALUE);
        this.mailBox.make();
    }

    public Optional<T> receive() {
        return this.mailBox.receive();
    }

    public void send(T message) {
        this.mailBox.send(message);
    }

    public void close() {
        this.mailBox.close();
    }
}
