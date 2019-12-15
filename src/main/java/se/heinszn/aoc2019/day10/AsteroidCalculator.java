package se.heinszn.aoc2019.day10;

import lombok.Value;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class AsteroidCalculator {
    private final int width;
    private final int height;

    private Map<Coord, Long> asteroids = new HashMap<>();
    private List<Coord> destroyed = new ArrayList<>();

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
        asteroids.keySet().forEach(coord -> {
            List<Coord> visibleFromCoord = getVisibleFromCoord(coord);
            asteroids.put(coord, (long) visibleFromCoord.size());
        });

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

    public List<Coord> removeVisibleFromCoord(Coord coord) {
        List<Coord> visibleFromCoord = getVisibleFromCoord(coord);

        ToDoubleFunction<Coord> comparator = c -> {
            double rad = Math.PI / 2 + Math.atan2(c.getY() - coord.getY(), c.getX() - coord.getX());
            if (rad < 0.0) {
                rad = Math.PI * 2 + rad;
            }
            return rad;
        };

        visibleFromCoord.sort(Comparator.comparingDouble(comparator));

        visibleFromCoord.forEach(c -> {
            destroyed.add(c);
            asteroids.remove(c);
        });
        return visibleFromCoord;
    }

    public long getAsteroidsSeenFromCoord(Coord coord) {
        return asteroids.getOrDefault(coord, 0L);
    }

    private List<Coord> getVisibleFromCoord(Coord asteroid) {
        return asteroids.keySet().stream()
                .filter(coord -> !asteroid.equals(coord))
                .filter(coord -> isVisible(asteroid, coord))
                .collect(Collectors.toList());
    }

    private boolean isVisible(Coord from, Coord to) {
        boolean viewObstructed;
        List<Coord> others = asteroids.keySet().stream()
                .filter(coord -> !coord.equals(from) && !coord.equals(to))
                .filter(coord -> coord.getX() <= Math.max(from.getX(), to.getX()))
                .filter(coord -> coord.getX() >= Math.min(from.getX(), to.getX()))
                .filter(coord -> coord.getY() <= Math.max(from.getY(), to.getY()))
                .filter(coord -> coord.getY() >= Math.min(from.getY(), to.getY()))
                .collect(Collectors.toList());

        if (from.getX() != to.getX()) {
            if (from.getY() == to.getY()) {
                viewObstructed = others.stream().map(Coord::getY).anyMatch(y -> from.getY() == y);
            } else {
                double[] mAndB = getLineEq(from, to);

                viewObstructed = others.stream()
                        .anyMatch(coord -> isOnLine(from, coord, mAndB[0], mAndB[1]));
            }
        } else {
            viewObstructed = others.stream().map(Coord::getX).anyMatch(x -> from.getX() == x);
        }

        return !viewObstructed;
    }

    private boolean isOnLine(Coord from, Coord coord, double m, double b) {
        double[] mAndB = getLineEq(from, coord);
        return m == mAndB[0] && b == mAndB[1];
    }

    private double[] getLineEq(Coord from, Coord to) {
        double m = (from.getX() - to.getX()) * 1.0 / (from.getY() - to.getY());
        return new double[] {
                m,
                from.getY() * 1.0 - m * from.getX()
        };
    }

    @Value(staticConstructor = "of")
    static class Coord {
        int x;
        int y;
    }
}
