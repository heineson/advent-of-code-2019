package se.heinszn.aoc2019.day6;

import lombok.Value;
import se.heinszn.aoc2019.common.Tree;

import java.util.List;
import java.util.stream.Collectors;

public class OrbitMapChecker {
    Tree<String> orbitTree;

    public OrbitMapChecker(List<String> orbitMap) {
        orbitTree = new Tree<>("COM");

        List<Orbit> orbits = orbitMap.stream().map(Orbit::fromString).collect(Collectors.toList());
        addOrbits(orbits, "COM");
    }

    private void addOrbits(List<Orbit> remaining, String parent) {
        List<Orbit> newRemaining = remaining.stream()
                .filter(o -> !parent.equals(o.getParent()))
                .collect(Collectors.toList());

        remaining.stream().filter(o -> o.getParent().equals(parent)).forEach(o -> {
            orbitTree.addValue(parent, o.getValue());
            addOrbits(newRemaining, o.getValue());
        });
    }

    public int getAllOrbits() {
        int orbits = 0;
        for (Tree.Node<String> node : orbitTree.getAllNodes()) {
            Tree.Node<String> current = node;
            while (current.getParent() != null) {
                orbits++;
                current = current.getParent();
            }
        }
        return orbits;
    }

    public int getTransfers(String from, String to) {
        Tree.Node<String> fromNode = orbitTree.findNode(from).getParent();
        Tree.Node<String> toNode = orbitTree.findNode(to).getParent();
        if (fromNode == null || toNode == null) {
            throw new IllegalArgumentException("From or to did not orbit any object, from: " + from + ", to: " + to);
        }
        Tree.Node<String> ancestor = orbitTree.closestCommonAncestor(fromNode.getValue(), toNode.getValue());
        int i1 = orbitTree.distanceToAncestor(ancestor, fromNode);
        int i2 = orbitTree.distanceToAncestor(ancestor, toNode);
        return i1 + i2;
    }

    @Value
    static class Orbit {
        String parent;
        String value;

        public static Orbit fromString(String asString) {
            String[] parts = asString.split("\\)");
            return new Orbit(parts[0], parts[1]);
        }
    }
}
