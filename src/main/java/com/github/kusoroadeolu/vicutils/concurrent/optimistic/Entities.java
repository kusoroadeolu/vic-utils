package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

public class Entities {

    private Entities(){
        throw new AssertionError();
    }

    <E>Entity<E> spawnEntity(E e){
        return new OptimisticEntity<>(e);
    }
}
