package se.heinszn.aoc2019.day14;

import java.util.List;

public class Day14 {
    public static void main(String[] args) {
        Factory factory = new Factory(TEST1);
    }

    static List<String> TEST1 = List.of(
            "10 ORE => 10 A",
            "1 ORE => 1 B",
            "7 A, 1 B => 1 C",
            "7 A, 1 C => 1 D",
            "7 A, 1 D => 1 E",
            "7 A, 1 E => 1 FUEL"
    );
}
