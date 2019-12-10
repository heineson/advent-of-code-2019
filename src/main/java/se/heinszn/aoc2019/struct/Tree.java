package se.heinszn.aoc2019.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ToString
public class Tree<T> {
    private Map<T, Node<T>> lookupMap = new HashMap<>();

    @Getter
    private Node<T> root;

    public Tree(T rootValue) {
        Node<T> rootNode = Node.ofValue(null, rootValue);
        this.root = rootNode;
        lookupMap.put(rootValue, rootNode);
    }

    public Node<T> findNode(T value) {
        return lookupMap.get(value);
    }

    public Node<T> addValue(T parent, T value) {
        Node<T> parentNode = findNode(parent);
        if (parentNode == null) {
            throw new IllegalArgumentException("Parent node not found: " + parent);
        }
        Node<T> newNode = Node.ofValue(parentNode, value);
        lookupMap.put(newNode.getValue(), newNode);
        return newNode;
    }

    public Set<Node<T>> getAllNodes() {
        return Set.copyOf(lookupMap.values());
    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class Node<T> {
        private T value;
        private Node<T> parent;

        public static <T> Node<T> ofValue(Node<T> parent, T value) {
            return new Node<>(value, parent);
        }
    }
}
