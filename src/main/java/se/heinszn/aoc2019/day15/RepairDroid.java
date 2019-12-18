package se.heinszn.aoc2019.day15;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import se.heinszn.aoc2019.common.IntcodeExecutor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class RepairDroid {
    private static final Random random = new Random();

    private ByteBuffer inputData;
    private ByteArrayOutputStream outputStream;
    private final IntcodeExecutor executor;

    private Position currentPos;
    private Set<Position> walls;
    private Set<Position> visited;

    public RepairDroid(Path program) throws IOException {
        this.inputData = ByteBuffer.allocate(100_000_000 * 4);
        outputStream = new ByteArrayOutputStream(65_536);
        this.executor = new IntcodeExecutor(program, new ByteArrayInputStream(this.inputData.array()), outputStream);
        this.executor.setPauseOnOutput(true);

        this.currentPos = Position.of(0, 0);
        this.walls = new HashSet<>();
        this.visited = new HashSet<>();
    }

    public void printMap() {
        int minX = walls.stream().mapToInt(Position::getX).min().getAsInt();
        int maxX = walls.stream().mapToInt(Position::getX).max().getAsInt();
        int minY = walls.stream().mapToInt(Position::getY).min().getAsInt();
        int maxY = walls.stream().mapToInt(Position::getY).max().getAsInt();

        StringBuilder sb = new StringBuilder();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (walls.contains(Position.of(x, y))) {
                    sb.append("#");
                } else if (currentPos.equals(Position.of(x, y))) {
                    sb.append("o");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public List<Position> solveBfs() {
        LinkedList<Position> nextToVisit = new LinkedList<>();
        Position start = Position.of(0, 0);
        currentPos = start;
        nextToVisit.add(start);

        while (!nextToVisit.isEmpty()) {
            Position cur = nextToVisit.remove();

            if (!currentPos.equals(cur)) {
                int statusCode = advanceTo(cur);
                switch (statusCode) {
                    case 0:
                        walls.add(cur);
                        this.visited.add(cur);
                        cur = currentPos; // reset, as the droid never moves
                        break;
                    case 1:
                        currentPos = cur;
                        break;
                    case 2:
                        currentPos = cur;
                        return backtrackPath(cur);
                    default:
                        throw new IllegalStateException("Unknown status code: " + statusCode);
                }
            }

            if (visited.contains(cur)) {
                continue;
            }

//            if (maze.isWall(cur.getX(), cur.getY())) {
//                visited.add(cur);
//                continue;
//            }
            visited.add(cur);
            for (Direction d : Direction.values()) {
                Position coordinate = cur.addDirection(d);
                nextToVisit.add(coordinate);
            }
        }
        System.out.println("WHY?");
        return Collections.emptyList();
    }

    private int advanceTo(Position position) {
        Direction d = Direction.get(currentPos, position);
        inputData.putInt(d.asInt());

        executor.execute();
        if (executor.isExited()) {
            throw new IllegalStateException("Program should not exit by itself");
        }

        return readOutput();
    }

    private List<Position> backtrackPath(Position cur) {
        List<Position> path = new ArrayList<>();
        Position iter = cur;

        while (iter != null) {
            path.add(iter);
            iter = iter.getParent();
        }

        return path;
    }

    private int readOutput() {
        byte[] bytes = outputStream.toByteArray();
        outputStream.reset();
        return new BigInteger(bytes).intValueExact();
    }

    @Value(staticConstructor = "of")
    static class Position {
        int x;
        int y;
        @EqualsAndHashCode.Exclude
        Position parent;

        static Position of(int x, int y) {
            return Position.of(x, y, null);
        }

        public Position addDirection(Direction d) {
            switch (d) {
                case N: return Position.of(x, y + 1, this);
                case S: return Position.of(x, y - 1, this);
                case W: return Position.of(x - 1, y, this);
                case E: return Position.of(x + 1, y, this);
                default: throw new IllegalArgumentException("Unknown direction: " + d);
            }
        }
    }

    enum Direction {
        N(1), S(2), W(3), E(4);

        private int i;

        Direction(int i) {
            this.i = i;
        }

        public int asInt() {
            return i;
        }

        static Direction fromInt(int d) {
            switch (d) {
                case 1: return N;
                case 2: return S;
                case 3: return W;
                case 4: return E;
                default: throw new IllegalArgumentException("Unknown direction: " + d);
            }
        }

        static Direction get(Position from, Position to) {
            int xdiff = from.getX() - to.getX();
            int ydiff = from.getY() - to.getY();

            if (xdiff < 0) {
                return E;
            } else if (xdiff > 0) {
                return W;
            } else if (ydiff < 0) {
                return N;
            } else if (ydiff > 0) {
                return S;
            } else {
                throw new IllegalArgumentException("Points not off by one in x or y: " + from + ", " + to);
            }
        }
    }
}
