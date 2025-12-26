package com.github.kusoroadeolu.vicutils.concurrent.actors;

@FunctionalInterface
public interface Behaviour<T> {

    Behaviour<T> change(T message);

    //Bsink = rec(b:m:ready(b)) -> Empty a behaviour which becomes itself. Basically a behaviour which doesn't do anything
    @SuppressWarnings("unchecked")
    static <T>Behaviour<T> sink(){
        return Sink.getSink();
    }

    @SuppressWarnings("unchecked")
    static <T>Behaviour<T> same(){
        return new Same<>();
    }

    @SuppressWarnings("unchecked")
    class Sink<T> implements Behaviour<T>{
        public static <T> Sink<T> getSink(){
            return (Sink<T>) SinkHolder.SINK;
        }

        @Override
        public Behaviour<T> change(T message) {
            return this;
        }

        static class SinkHolder {
            private final static Behaviour<?> SINK = new Sink<>();
        }
    }

    //A behaviour used to mimic the prev behaviour. It basically does n
    @SuppressWarnings("unchecked")
    class Same<T> implements Behaviour<T>{
        public static <T> Same<T> getEmpty(){
            return (Same<T>) EmptyHolder.SAME;
        }

        @Override
        public Behaviour<T> change(T message) {
            return null;
        }

        static class EmptyHolder {
            private final static Behaviour<?> SAME = new Same<>();
        }
    }

}
