package se.heinszn.aoc2019.day12;

import java.util.Map;

public class Day12 {

    public static void main(String[] args) {
        test1();
        test2();
        part1();
    }

    static void test1() {
        MoonSimulator simulator = new MoonSimulator(Map.of(
                MoonSimulator.Moon.I, MoonSimulator.Position.of(-1, 0, 2),
                MoonSimulator.Moon.E, MoonSimulator.Position.of(2, -10, -7),
                MoonSimulator.Moon.G, MoonSimulator.Position.of(4, -8, 8),
                MoonSimulator.Moon.C, MoonSimulator.Position.of(3, 5, -1)
        ));
        simulator.nextTimeStep();
        simulator.nextTimeStep();
        simulator.nextTimeStep();
        simulator.nextTimeStep();
        simulator.nextTimeStep();
        simulator.nextTimeStep();
        simulator.nextTimeStep();
        simulator.nextTimeStep();
        simulator.nextTimeStep();
        simulator.nextTimeStep();
        printStep(simulator.getMoonPositions());
        System.out.println("Total energy after 10 steps: " + simulator.calculateEnergy());
    }

    static void test2() {
        MoonSimulator simulator = new MoonSimulator(Map.of(
                MoonSimulator.Moon.I, MoonSimulator.Position.of(-8, -10, 0),
                MoonSimulator.Moon.E, MoonSimulator.Position.of(5, 5, 10),
                MoonSimulator.Moon.G, MoonSimulator.Position.of(2, -7, 3),
                MoonSimulator.Moon.C, MoonSimulator.Position.of(9, -8, -3)
        ));
        for (int i = 0; i < 100; i++) {
            simulator.nextTimeStep();
        }
        printStep(simulator.getMoonPositions());
        System.out.println("Total energy after 100 steps: " + simulator.calculateEnergy());
    }

    static void part1() {
        MoonSimulator simulator = new MoonSimulator(Map.of(
                MoonSimulator.Moon.I, MoonSimulator.Position.of(4, 1, 1),
                MoonSimulator.Moon.E, MoonSimulator.Position.of(11, -18, -1),
                MoonSimulator.Moon.G, MoonSimulator.Position.of(-2, -10, -4),
                MoonSimulator.Moon.C, MoonSimulator.Position.of(-7, -2, 14)
        ));
        for (int i = 0; i < 1000; i++) {
            simulator.nextTimeStep();
        }
        System.out.println("\nPart 1:");
        printStep(simulator.getMoonPositions());
        System.out.println("Total energy after 1000 steps: " + simulator.calculateEnergy());
    }

    static void printStep(Map<MoonSimulator.Moon, MoonSimulator.Position> moonPositions) {
        System.out.println(moonPositions.get(MoonSimulator.Moon.I));
        System.out.println(moonPositions.get(MoonSimulator.Moon.E));
        System.out.println(moonPositions.get(MoonSimulator.Moon.G));
        System.out.println(moonPositions.get(MoonSimulator.Moon.C));
        System.out.println();
    }
}
