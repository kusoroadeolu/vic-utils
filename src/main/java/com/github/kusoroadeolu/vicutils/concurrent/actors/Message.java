package com.github.kusoroadeolu.vicutils.concurrent.actors;

import java.util.function.Consumer;

public record Message<T>(Consumer<T> message) {
}