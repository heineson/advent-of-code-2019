package se.heinszn.aoc2019.day13;

import lombok.Value;
import se.heinszn.aoc2019.common.IntcodeExecutor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ArcadeGamePlayer {
    private ByteBuffer inputData;
    private ByteArrayOutputStream outputStream;
    private final IntcodeExecutor executor;

    private Map<Point, TileType> screen;

    public ArcadeGamePlayer(Path program) throws Exception {
        this.inputData = ByteBuffer.allocate(65_536);
        outputStream = new ByteArrayOutputStream(65_536);
        this.executor = new IntcodeExecutor(program, new ByteArrayInputStream(this.inputData.array()), outputStream);
        this.executor.setPauseOnOutput(true);

        screen = new HashMap<>();
    }

    public void run(int quarters) {
        if (quarters > 0) {
            executor.setMemoryAddress(0, BigInteger.valueOf(quarters));
        }

        while (!executor.isExited()) {
            executor.execute();
            if (executor.isExited()) {
                break;
            }
            int x = readOutput();

            executor.execute();
            int y = readOutput();

            executor.execute();
            int tileType = readOutput();

            screen.put(Point.of(x, y), TileType.ofInt(tileType));
        }
    }

    private int readOutput() {
        byte[] bytes = outputStream.toByteArray();
        outputStream.reset();
        return new BigInteger(bytes).intValueExact();
    }

    public Map<Point, TileType> getScreen() {
        return screen;
    }

    enum TileType {
        EMPTY, WALL, BLOCK, PADDLE, BALL;

        static TileType ofInt(int type) {
            switch (type) {
                case 0: return EMPTY;
                case 1: return WALL;
                case 2: return BLOCK;
                case 3: return PADDLE;
                case 4: return BALL;
                default: throw new IllegalArgumentException("Unknown tile type " + type);
            }
        }
    }

    @Value(staticConstructor = "of")
    static class Point {
        int x;
        int y;
    }
}
