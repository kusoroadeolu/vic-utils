package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

public class Entities {

    private Entities(){
        throw new AssertionError();
    }

    public static  <E>Entity<E> spawnEntity(E e){
        return new OptimisticEntity<>(e);
    }
}
