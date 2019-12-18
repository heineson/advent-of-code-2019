package se.heinszn.aoc2019.day14;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 {
    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();
        part1();
        part2();
    }

    static void test1() {
        Factory factory = new Factory(TEST1);
        long ore = factory.getOreForOneFuel();
        System.out.println("Test1: " + ore);
    }

    static void test2() {
        Factory factory = new Factory(TEST2);
        long ore = factory.getOreForOneFuel();
        System.out.println("Test2: " + ore);
    }

    static void test3() {
        Factory factory = new Factory(TEST3);
        long ore = factory.getOreForOneFuel();
        System.out.println("Test3: " + ore);
    }

    static void part1() throws Exception {
        URI resource = Day14.class.getClassLoader().getResource("day14.txt").toURI();
        Stream<String> lines = Files.lines(Path.of(resource));

        Factory factory = new Factory(lines.collect(Collectors.toList()));
        long ore = factory.getOreForOneFuel();
        System.out.println("Part 1: " + ore);
        System.out.println("Part 1 leftovers: " + factory.getLeftoverAmounts());
    }

    static void part2() throws Exception {
        URI resource = Day14.class.getClassLoader().getResource("day14.txt").toURI();
        List<String> lines = Files.lines(Path.of(resource)).collect(Collectors.toList());

        // Do 1000 iters to get acc ore and leftovers
        Factory factory = new Factory(lines);
        for (int i = 0; i < 1000; i++) {
            factory.getOreForOneFuel();
        }
        Map<String, Long> leftoverAmounts = factory.getLeftoverAmounts();
        long orePer1000Iter = factory.getOre();

        int startItersOf1000 = 2500;

        long fuel = startItersOf1000 * 1000;
        long startOre = orePer1000Iter  * startItersOf1000;
        final Map<String, Long> initLeftovers = new HashMap<>();
        leftoverAmounts.forEach((s, a) -> {
            initLeftovers.put(s, a * startItersOf1000);
        });

        System.out.println("Starting part2 with " + startOre + " spent and leftovers: " + initLeftovers);
        factory = new Factory(lines, initLeftovers);
        long accOreUsed = 0L;
        long lastPrint = System.currentTimeMillis();
        while (startOre + accOreUsed < 1000000000000L) {
            accOreUsed = factory.getOreForOneFuel();
            fuel++;
            if (System.currentTimeMillis() - lastPrint > 30_000) {
                lastPrint = System.currentTimeMillis();
                System.out.println("Acc ore: " + ((startOre + accOreUsed) / 1_000_000) + "M");
            }
        }

        System.out.println("Part 2: " + fuel + " (ore: " + (startOre + accOreUsed) + ")");
        System.out.println("Part 2 leftovers: " + factory.getLeftoverAmounts());
    }

    static List<String> TEST1 = List.of(
            "10 ORE => 10 A",
            "1 ORE => 1 B",
            "7 A, 1 B => 1 C",
            "7 A, 1 C => 1 D",
            "7 A, 1 D => 1 E",
            "7 A, 1 E => 1 FUEL"
    );

    static List<String> TEST2 = List.of(
            "9 ORE => 2 A",
            "8 ORE => 3 B",
            "7 ORE => 5 C",
            "3 A, 4 B => 1 AB",
            "5 B, 7 C => 1 BC",
            "4 C, 1 A => 1 CA",
            "2 AB, 3 BC, 4 CA => 1 FUEL"
    );

    static List<String> TEST3 = List.of(
            "171 ORE => 8 CNZTR",
            "7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL",
            "114 ORE => 4 BHXH",
            "14 VRPVC => 6 BMBT",
            "6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL",
            "6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT",
            "15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW",
            "13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW",
            "5 BMBT => 4 WPTQ",
            "189 ORE => 9 KTJDG",
            "1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP",
            "12 VRPVC, 27 CNZTR => 2 XDBXC",
            "15 KTJDG, 12 BHXH => 5 XCVML",
            "3 BHXH, 2 VRPVC => 7 MZWV",
            "121 ORE => 7 VRPVC",
            "7 XCVML => 6 RJRHP",
            "5 BHXH, 4 VRPVC => 5 LTCX"
    );
}
