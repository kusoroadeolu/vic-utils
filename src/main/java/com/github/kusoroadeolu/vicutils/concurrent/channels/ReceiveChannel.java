package com.github.kusoroadeolu.vicutils.concurrent.channels;

import java.util.Optional;

public interface ReceiveChannel<T> extends UniDirectionalChannel<T> {
    Optional<T> receive();
    T tryReceive();

}
