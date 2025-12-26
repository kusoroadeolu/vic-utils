package com.github.kusoroadeolu.vicutils.concurrent.actors;

public class ChildDeathException extends RuntimeException {
    public ChildDeathException() {
        super();
    }

    public ChildDeathException(String message) {
        super(message);
    }
}
