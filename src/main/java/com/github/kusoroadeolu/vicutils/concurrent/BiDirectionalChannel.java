package com.github.kusoroadeolu.vicutils.concurrent;

public interface BiDirectionalChannel<T> extends ReceiveChannel<T>, SendChannel<T>{
    T receive();

    SendChannel<T> makeSendChannel();

    ReceiveChannel<T> makeReceiveChannel();

    void send(T val);
}
