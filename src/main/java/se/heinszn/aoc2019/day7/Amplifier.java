package se.heinszn.aoc2019.day7;

import se.heinszn.aoc2019.common.IntcodeExecutorOld;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

public class Amplifier {
    private IntcodeExecutorOld executor;
    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;

    public Amplifier(int[] program, int[] input) throws IOException  {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 * input.length);
        for (int i : input) {
            byteBuffer.putInt(i);
        }
        this.inputStream = new ByteArrayInputStream(byteBuffer.array());
        this.outputStream = new ByteArrayOutputStream();
        executor = new IntcodeExecutorOld(program, inputStream, outputStream);
        executor.setPauseOnOutput(true);
    }

    public Amplifier(int[] program, ByteArrayInputStream inputStream) throws IOException  {
        this.inputStream = inputStream;
        this.outputStream = new ByteArrayOutputStream(4 * 10_000);
        executor = new IntcodeExecutorOld(program, this.inputStream, outputStream);
        executor.setPauseOnOutput(true);
    }

    public Optional<Integer> runUntilOutput() {
        executor.execute();
        byte[] bytes = outputStream.toByteArray();
        if (bytes.length < Integer.BYTES) {
            return Optional.empty();
        }
        outputStream.reset();
        return Optional.of(ByteBuffer.wrap(bytes).getInt());
    }

    public boolean isFinished() {
        return executor.isExited();
    }
}
