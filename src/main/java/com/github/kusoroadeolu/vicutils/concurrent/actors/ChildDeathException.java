package com.github.kusoroadeolu.vicutils.concurrent.actors;

public class ChildDeathException extends RuntimeException {
    public ChildDeathException(Throwable cause) {
        super(cause);
    }
}
