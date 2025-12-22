package com.github.kusoroadeolu.vicutils.concurrent.actors;

import com.github.kusoroadeolu.vicutils.concurrent.channels.BufferedChannel;
import com.github.kusoroadeolu.vicutils.concurrent.channels.Channel;

import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

public class Actor<T> {
    private final T t;
    private final Channel<Consumer<T>> mailbox;

    public Actor(T t, int mailboxSize) {
        if (mailboxSize < 0) throw new IllegalArgumentException("mailboxSize < 0");
        this.t = t;
        this.mailbox = new BufferedChannel<>(mailboxSize);

    }




}
