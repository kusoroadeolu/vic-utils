package com.github.kusoroadeolu.vicutils.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferTest {


    @Test
    public void onBufferFull_shouldOverwritePreviousHead(){
        RingBuffer<Integer> ringBuffer = new RingBuffer<>(3);
        for (int i = 0; i < 4; ++i){
            ringBuffer.add(i);
        }

        assertEquals(2, ringBuffer.getLast());
        assertEquals(3, ringBuffer.head());
    }

    @Test
    public void onBufferNotFully_headShouldBeStartingIndex(){
        RingBuffer<Integer> ringBuffer = new RingBuffer<>(3);
        for (int i = 0; i < 2; ++i){
            ringBuffer.add(i);
        }

        assertEquals(0, ringBuffer.head());
    }

    @Test
    public void shouldIterateProperly(){
        RingBuffer<Integer> ringBuffer = new RingBuffer<>(3);
        for (int i = 0; i < 3; ++i){
            ringBuffer.add(i);
        }

        int idx = -1;
        for (int i : ringBuffer){
            assertEquals(++idx, i);
        }
    }
}