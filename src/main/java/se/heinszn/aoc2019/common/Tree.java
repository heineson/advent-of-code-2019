package se.heinszn.aoc2019.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

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

    public List<Node<T>> getPathToRoot(T value) {
        List<Node<T>> list = new ArrayList<>();
        Node<T> current = findNode(value);
        while (current != null) {
            list.add(current);
            current = current.getParent();
        }
        return list;
    }

    public Node<T> closestCommonAncestor(T v1, T v2) {
        List<Node<T>> node1ToRoot = getPathToRoot(v1);
        Node<T> current = findNode(v2);
        while (!node1ToRoot.contains(current)) {
            current = current.getParent();
        }
        return current;
    }

    public int distanceToAncestor(Node<T> ancestor, Node<T> startPoint) {
        Node<T> current = startPoint;
        int steps = 0;
        while (current.getValue() != ancestor.getValue()) {
            steps++;
            current = current.getParent();
        }
        return steps;
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
