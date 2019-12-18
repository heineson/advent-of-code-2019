package se.heinszn.aoc2019.day15;

import java.net.URI;
import java.nio.file.Path;

public class Day15 {
    public static void main(String[] args) throws Exception {
        URI resource = Day15.class.getClassLoader().getResource("day15.txt").toURI();
        RepairDroid repairDroid = new RepairDroid(Path.of(resource));
        try {
            System.out.println(repairDroid.solveBfs());
        } finally {
            repairDroid.printMap();
        }
    }
}
