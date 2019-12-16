package se.heinszn.aoc2019.day11;

import lombok.Value;
import se.heinszn.aoc2019.common.IntcodeExecutor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PaintingRobot {
    public static final int BLACK = 0;
    public static final int WHITE = 1;

    private ByteBuffer inputData;
    private ByteArrayOutputStream outputStream;
    private final IntcodeExecutor executor;

    private Direction currDirection;
    private Coord currPos;
    private Map<Coord, Integer> paintedPanels;

    public PaintingRobot(Path program) throws IOException {
        this.inputData = ByteBuffer.allocate(65_536);
        outputStream = new ByteArrayOutputStream(65_536);
        this.executor = new IntcodeExecutor(program, new ByteArrayInputStream(this.inputData.array()), outputStream);
        this.executor.setPauseOnOutput(true);

        this.currDirection = Direction.U;
        this.currPos = Coord.of(0,0);
        this.paintedPanels = new HashMap<>();
    }

    public void run() {
        while (!executor.isExited()) {
            // prepare input
            int color = paintedPanels.getOrDefault(currPos, BLACK);
            inputData.putInt(color);

            // run until 1st output
            executor.execute();
            if (executor.isExited()) {
                break;
            }
            int newColor = readOutput();

            // run until 2nd output
            executor.execute();
            int turn = readOutput();

            // paint, turn and move
            paintedPanels.put(currPos, newColor);
            this.currDirection = currDirection.turn(turn);
            this.currPos = currDirection.next(currPos);
        }
    }

    public Map<Coord, Integer> getPaintedPanels() {
        return new HashMap<>(this.paintedPanels);
    }

    private int readOutput() {
        byte[] bytes = outputStream.toByteArray();
        outputStream.reset();
        return new BigInteger(bytes).intValueExact();
    }

    enum Direction {
        U, R, D, L;

        Coord next(Coord pos) {
            switch (this) {
                case U: return Coord.of(pos.getX(), pos.getY() + 1);
                case R: return Coord.of(pos.getX() + 1, pos.getY());
                case D: return Coord.of(pos.getX(), pos.getY() - 1);
                case L: return Coord.of(pos.getX() - 1, pos.getY());
                default: throw new IllegalStateException();
            }
        }

        Direction turn(int turn) { // 0 == left 90
            switch (this) {
                case U: return turn == 0 ? L : R;
                case R: return turn == 0 ? U : D;
                case D: return turn == 0 ? R : L;
                case L: return turn == 0 ? D : U;
                default: throw new IllegalStateException();
            }
        }
    }

    @Value(staticConstructor = "of")
    static class Coord {
        int x;
        int y;
    }
}
