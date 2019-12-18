package se.heinszn.aoc2019.day15;

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
    private Direction currDirection;
    private Set<Position> walls;
    private Set<Position> visited;

    public RepairDroid(Path program) throws IOException {
        this.inputData = ByteBuffer.allocate(80_000_000);
        outputStream = new ByteArrayOutputStream(65_536);
        this.executor = new IntcodeExecutor(program, new ByteArrayInputStream(this.inputData.array()), outputStream);
        this.executor.setPauseOnOutput(true);

        this.currentPos = Position.of(0, 0);
        this.currDirection = Direction.N;
        this.walls = new HashSet<>();
        this.visited = new HashSet<>();
        this.visited.add(this.currentPos);
    }

    public Position findOxygenTankPos() {
        run(-1, -1);
        return currentPos;
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

    /**
     * Steps per loop:
     * - Accept a movement command via an input instruction.
     * - Send the movement command to the repair droid.
     * - Wait for the repair droid to finish the movement operation.
     * - Report on the status of the repair droid via an output instruction.
     */
    private void run(int x, int y) {
        if (x != -1 || y != -1) {
        }

        boolean done = false;
        long loops = 0;
        while (!done) {
            inputData.putInt(this.currDirection.asInt());

            executor.execute();
            if (executor.isExited()) {
                break;
            }

            int statusCode = readOutput();
            Position nextPosition = this.currentPos.addDirection(this.currDirection);
            switch (statusCode) {
                case 0:
                    walls.add(nextPosition);
                    newDirection();
                    break;
                case 1:
                    currentPos = nextPosition;
                    this.visited.add(currentPos);
                    break;
                case 2:
                    currentPos = nextPosition;
                    this.visited.add(currentPos);
                    done = true;
                    break;
                default: throw new IllegalStateException("Unknown status code: " + statusCode);
            }
            if (++loops % 1_000_000 == 0) {
                System.out.println("  loops: " + loops);
            }
        }
    }
// TODO Try BFS algo: https://www.baeldung.com/java-solve-maze
    private void newDirection() {
        List<Direction> possible = Arrays.stream(Direction.values())
                .filter(d -> !walls.contains(currentPos.addDirection(d)))
                .collect(Collectors.toList());
        List<Direction> preferred = possible.stream()
                .filter(d -> !visited.contains(currentPos.addDirection(d)))
                .collect(Collectors.toList());

        Direction randomDir;
        if (preferred.size() > 0) {
            randomDir = preferred.get(random.nextInt(preferred.size()));
        } else {
            randomDir = possible.get(random.nextInt(possible.size()));
        }
//        do {
//            randomDir = Direction.fromInt(random.nextInt(4) + 1);
//        } while (randomDir == this.currDirection || walls.contains(this.currentPos.addDirection(randomDir)));
        this.currDirection = randomDir;

//        this.currDirection = Arrays.stream(Direction.values())
//                .filter(d -> !walls.contains(currentPos.addDirection(d)))
//                .max(this::compareDirs).get();
//        System.out.println(this.currDirection + ", " + Arrays.stream(Direction.values())
//                .filter(d -> !walls.contains(currentPos.addDirection(d)))
//                .sorted(this::compareDirs).collect(Collectors.toList()));
    }

//    private int compareDirs(Direction d1, Direction d2) {
//        int d1Score = visited.contains(this.currentPos.addDirection(d1)) ? 1000 : 10;
//        int d2Score = visited.contains(this.currentPos.addDirection(d1)) ? 1000 : 10;
//
//        return Integer.compare(d1Score, d2Score);
//    }

    private int readOutput() {
        byte[] bytes = outputStream.toByteArray();
        outputStream.reset();
        return new BigInteger(bytes).intValueExact();
    }

    @Value(staticConstructor = "of")
    static class Position {
        int x;
        int y;

        public Position addDirection(Direction d) {
            switch (d) {
                case N: return Position.of(x, y + 1);
                case S: return Position.of(x, y - 1);
                case W: return Position.of(x - 1, y);
                case E: return Position.of(x + 1, y);
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
    }
}
