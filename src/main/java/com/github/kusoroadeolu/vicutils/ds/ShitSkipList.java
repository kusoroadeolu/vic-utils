package com.github.kusoroadeolu.vicutils.ds;

import java.util.*;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/*
* where 0 -> top layer
* and layers#size - 1 -> bottom layer
* */

//A very shitty skip list implementation
//Use this at your own risk
public class ShitSkipList<E extends Comparable<E>> {
    private final static String MESSAGE = "e != null";
    private final double probability;
    private final Node<E> head;
    private final List<List<Node<E>>> layers;
    private final NodeComparator<Node<E>> nodeComparator;

    public ShitSkipList(double probability) {
        if (probability > 1) throw new IllegalArgumentException();
        this.probability = probability;
        this.layers = this.populateLayers();
        this.nodeComparator = new NodeComparator<>();
        this.head = new Node<>(null, new HashMap<>());


    }

    public ShitSkipList(){
        this(0.5);
    }

     List<List<Node<E>>> populateLayers(){
        int layerCount = (int) (1.0 / (1.0 - this.probability));
        if (layerCount == 0) layerCount = 1;
        final List<ArrayList<Node<E>>> list = new ArrayList<>(layerCount);
        for (int i = 0; i < layerCount; ++i){
            list.add(new ArrayList<>());
        }

        return unmodifiableList(list);
     }

    public void add(E e){
        requireNonNull(e, MESSAGE);

        Node<E> node = null;
        for (int i = layers.size() - 1; i >= 0; --i){
            List<Node<E>> layer = layers.get(i);
            if (node == null)  node = new Node<>(e, new HashMap<>());
            layer.add(node);
            layer.sort(nodeComparator);
            int index = layer.indexOf(node);

            if (index - 1 >= 0){
                Node<E> prev = layer.get(index - 1);
                prev.map.put(i, node); //Ensure to update the node before this node
            }else{
                head.map.put(i, node);
            }

            if (index != (layer.size() - 1)){
                node.map.put(i, layer.get(++index)); //key(layer idx), value(next idx)
            }

            double ran = Math.random();
            if (ran > probability){
                break;
            }
        }
    }

    public void remove(E e){
        requireNonNull(e, MESSAGE);
        List<Node<E>> layer;
        Node<E> dummy = new Node<>(e, null);
        for (int i = layers.size() - 1; i >= 0; --i){
            layer = layers.get(i);
            int index = layer.indexOf(dummy);
            boolean removed = layer.remove(dummy);
            if (!removed) break; //If it wasn't removed at this layer, it doesn't exist above
            if (index > 0){
                Node<E> prev = layer.get(index - 1); //Get the previous node
                if (index < layer.size() - 1) prev.map.put(i, layer.get(index)); //Ensure this isn't the last node otherwise just remove it
                else prev.map.remove(i);
            }else{
                if (layer.isEmpty()) continue;
                head.map.put(i, layer.get(index));
            }
        }
    }

    public boolean contains(E e){
        requireNonNull(e, MESSAGE);
        for (int i = 0; i < layers.size(); ++i){
            Node<E> next = head.map.get(i);
            Node<E> dummy = new Node<>(e, null);
            do {
                if (next.equals(dummy)){
                    return true;
                }

                next = next.map.get(i);
            }while (next != null);
        }

        return false;
    }

    public int size(){
        return this.layers.getLast().size();
    }

    public boolean isEmpty(){
        return this.size() <= 0;
    }

    public E first(){
        return this.layers.getLast().getFirst().value;
    }

    public E last(){
        return this.layers.getLast().getLast().value;
    }



    //For testing
    List<List<Node<E>>> layers(){
        return unmodifiableList(layers);
    }

    //Layer(k), Idx(v)
    record Node<E extends Comparable<E>>(E value, Map<Integer, Node<E>> map) implements Comparable<Node<E>>{

        @Override
        public int compareTo(Node<E> e) {
            return value.compareTo(e.value);
        }

        @SuppressWarnings("unchecked")
        public boolean equals(Object other){
            if (other == null) return false;
            if(!(other instanceof ShitSkipList.Node<?> node)) return false;
            E val = (E) node.value;
            return value.equals(val);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    record NodeComparator<E extends Comparable<E>>() implements Comparator<E>{
        @Override
        public int compare(E o1, E o2) {
            return o1.compareTo(o2);
        }
    }
}
