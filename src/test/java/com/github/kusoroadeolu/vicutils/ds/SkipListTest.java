package com.github.kusoroadeolu.vicutils.ds;

import org.junit.jupiter.api.Test;

class SkipListTest {

    @Test
    public void random(){
        ShitSkipList<Integer> list = new ShitSkipList<>(0.9);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        IO.println(list.layers().getFirst());
    }
}