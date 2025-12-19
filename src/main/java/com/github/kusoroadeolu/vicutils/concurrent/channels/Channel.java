package com.github.kusoroadeolu.vicutils.concurrent.channels;

public interface Channel<T> extends ReceiveChannel<T>, SendChannel<T>{
    SendChannel<T> makeSendChannel();
    ReceiveChannel<T> makeReceiveChannel();
}
