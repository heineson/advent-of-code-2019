package se.heinszn.aoc2019.day3;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VectorIntersectFinder {

    private Set<Vector> set1;
    private Set<Vector> set2;

    public VectorIntersectFinder(Set<Vector> set1, Set<Vector> set2) {
        this.set1 = set1;
        this.set2 = set2;
    }

    public List<Point> getIntersections() {
        List<Point> intersections = new ArrayList<>();
        set1.forEach(v1 -> {
            set2.forEach(v2 -> {
                v1.isIntersecting(v2).ifPresent(intersections::add);
            });
        });
        return intersections.stream().filter(p -> !p.equals(Point.of(0, 0))).collect(Collectors.toList());
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

        List<Point> points = new ArrayList<>();
        List<Point> otherPoints = new ArrayList<>();

        if (vertical) {
            getRange(start.getY(), end.getY()).mapToObj(y -> Point.of(start.getX(), y)).forEach(points::add);
            getRange(other.start.getX(), other.end.getX()).mapToObj(x -> Point.of(x, other.start.getY())).forEach(otherPoints::add);
        } else {
            getRange(other.start.getY(), other.end.getY()).mapToObj(y -> Point.of(other.start.getX(), y)).forEach(otherPoints::add);
            getRange(start.getX(), end.getX()).mapToObj(x -> Point.of(x, start.getY())).forEach(points::add);
        }

        return points.stream().filter(otherPoints::contains).findAny();
    }

    private static IntStream getRange(int start, int end) {
        boolean isPositive = end > start;
        return IntStream.iterate(
                start,
                i -> isPositive ? i <= end : i >= end,
                i -> isPositive ? i + 1 : i - 1);
    }
}

enum Direction {
    U, D, L, R
}
