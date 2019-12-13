package se.heinszn.aoc2019.day10;

import lombok.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsteroidCalculator {
    private final int width;
    private final int height;

    private Map<Coord, Long> asteroids = new HashMap<>();

    public AsteroidCalculator(String map) {
        String[] lines = map.split("\n");
        height = lines.length;
        width = lines[0].length();
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '#') {
                    asteroids.put(Coord.of(x, y), null);
                }
            }
        }
    }

    public void printMap() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                long seen = asteroids.getOrDefault(Coord.of(x, y), 0L);
                sb.append(seen == 0 ? "." : "" + seen);
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public Coord calculateAll() {
        asteroids.keySet().forEach(coord -> asteroids.put(coord, calculateForCoord(coord)));

        long max = 0;
        Coord coord = null;
        for (Entry<Coord, Long> entry : asteroids.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                coord = entry.getKey();
            }
        }
        return coord;
    }

    public long getAsteroidsSeenFromCoord(Coord coord) {
        return asteroids.getOrDefault(coord, 0L);
    }

    private long calculateForCoord(Coord asteroid) {
        return asteroids.keySet().stream()
                .filter(coord -> !asteroid.equals(coord))
                .filter(coord -> isVisible(asteroid, coord))
                .count();
    }

    private boolean isVisible(Coord from, Coord to) {
        double m = from.getY() - to.getY() == 0
                ? 1.0
                : (from.getX() - to.getX()) * 1.0 / (from.getY() - to.getY());

        double b = from.getY() * 1.0 - m * from.getX();
        System.out.println("From: " + from + ", to: " + to + String.format("y=%f * x + %f", m, b));

        List<Coord> others = asteroids.keySet().stream()
                .filter(coord -> !coord.equals(from) && !coord.equals(to))
                .collect(Collectors.toList());
        boolean viewObstructed = others.stream()
                .anyMatch(coord -> isOnLine(coord, m, b));
        viewObstructed = viewObstructed || isOnVerticalLine(from, to, others); // TODO continue here
        return !viewObstructed;
    }

    private boolean isOnVerticalLine(Coord from, Coord to, List<Coord> others) {
        if (from.getX() - to.getX() != 0) {
            return false;
        }
        return others.stream().map(Coord::getX).anyMatch(y -> from.getX() == y);
    }

    private boolean isOnLine(Coord coord, double m, double b) {
        double y = coord.getY() * 1.0;
        double x = coord.getX() * 1.0;
        System.out.println("Coord: " + coord + ", y=" + y + ", mx+b=" + (m*x + b));
        return y == m*x + b;
    }

    @Value(staticConstructor = "of")
    static class Coord {
        int x;
        int y;
    }
}
