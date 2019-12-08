package se.heinszn.aoc2019.day3;

import lombok.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VectorIntersectFinder {

    private List<Vector> wire1;
    private List<Point> wire1Points;
    private List<Vector> wire2;
    private List<Point> wire2Points;

    public VectorIntersectFinder(List<Vector> wire1, List<Vector> wire2) {
        this.wire1 = wire1;
        this.wire1Points = getWirePoints(wire1);
        this.wire2 = wire2;
        this.wire2Points = getWirePoints(wire2);
    }

    public List<Point> getIntersections() {
        List<Point> intersections = new ArrayList<>();
        wire1.forEach(v1 -> {
            wire2.forEach(v2 -> {
                v1.isIntersecting(v2).ifPresent(intersections::add);
            });
        });
        return intersections.stream().filter(p -> !p.equals(Point.of(0, 0))).collect(Collectors.toList());
    }

    public Map<Point, Integer> getDistances(List<Point> intersections) {
        Map<Point, Integer> distances = new HashMap<>();
        intersections.forEach(i -> distances.put(i, wire1Points.indexOf(i) + wire2Points.indexOf(i)));
        return distances;
    }

    private List<Point> getWirePoints(List<Vector> wire) {
        List<Point> wire1Points = wire.stream()
                .flatMap(v -> {
                    List<Point> points = v.getPoints();
                    return points.subList(0, points.size() - 1).stream();
                })
                .collect(Collectors.toList());

        Point lastPoint = wire.get(wire.size() - 1).getEnd();
        wire1Points.add(Point.of(lastPoint.getX(), lastPoint.getY()));
        return wire1Points;
    }
}

@Value(staticConstructor = "of")
class Point {
    private int x, y;
}

@Value
class Vector {
    private Point start, end;
    private boolean vertical;

    public static Vector fromVectorAndDirection(Vector start, Direction d, int length) {
        switch (d) {
            case U:
                return new Vector(start.getEnd(), Point.of(start.getEnd().getX(), start.getEnd().getY() + length), true);
            case D:
                return new Vector(start.getEnd(), Point.of(start.getEnd().getX(), start.getEnd().getY() - length), true);
            case L:
                return new Vector(start.getEnd(), Point.of(start.getEnd().getX() - length, start.getEnd().getY()), false);
            case R:
                return new Vector(start.getEnd(), Point.of(start.getEnd().getX() + length, start.getEnd().getY()), false);
            default:
                throw new IllegalStateException("Unknown direction " + d);
        }
    }

    public Optional<Point> isIntersecting(Vector other) {
        if (vertical == other.vertical) {
            return Optional.empty();
        }

        return getPoints().stream().filter(other.getPoints()::contains).findAny();
    }

    private static IntStream getRange(int start, int end) {
        boolean isPositive = end > start;
        return IntStream.iterate(
                start,
                i -> isPositive ? i <= end : i >= end,
                i -> isPositive ? i + 1 : i - 1);
    }

    public List<Point> getPoints() {
        if (vertical) {
            return getRange(start.getY(), end.getY()).mapToObj(y -> Point.of(start.getX(), y)).collect(Collectors.toList());
        } else {
            return getRange(start.getX(), end.getX()).mapToObj(x -> Point.of(x, start.getY())).collect(Collectors.toList());
        }
    }
}

enum Direction {
    U, D, L, R
}
