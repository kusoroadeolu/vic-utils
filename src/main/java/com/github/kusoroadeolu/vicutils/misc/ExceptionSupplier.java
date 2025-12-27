package com.github.kusoroadeolu.vicutils.misc;

@FunctionalInterface
public interface ExceptionSupplier<T> {
    T supply() throws Exception;
}
