package se.heinszn.aoc2019.day7;

import se.heinszn.aoc2019.common.IntcodeExecutor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;

public class Amplifier {
    private IntcodeExecutor executor;
    private ByteArrayOutputStream outputStream;

    public Amplifier(int[] program, int[] input) throws IOException  {
        this.outputStream = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 * input.length);
        for (int i : input) {
            byteBuffer.putInt(i);
        }
        executor = new IntcodeExecutor(program, new ByteArrayInputStream(byteBuffer.array()), new PrintStream(outputStream));
    }

    public int amplify() {
        executor.execute();
        byte[] bytes = outputStream.toByteArray();
        return ByteBuffer.wrap(bytes).getInt();
    }
}
