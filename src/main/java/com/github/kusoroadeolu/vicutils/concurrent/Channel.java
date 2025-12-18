package com.github.kusoroadeolu.vicutils.concurrent;

import java.util.Optional;

public interface Channel<T> extends ReceiveChannel<T>, SendChannel<T>{
    Optional<T> receive();
    SendChannel<T> makeSendChannel();
    ReceiveChannel<T> makeReceiveChannel();
    void send(T val);
    boolean isEmpty();
    boolean ok();
}
