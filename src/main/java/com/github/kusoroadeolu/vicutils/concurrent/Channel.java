package com.github.kusoroadeolu.vicutils.concurrent;

public interface Channel<T> extends ReceiveChannel<T>, SendChannel<T>{
    T receive();

    SendChannel<T> makeSendChannel();

    ReceiveChannel<T> makeReceiveChannel();

    void send(T val);
}
