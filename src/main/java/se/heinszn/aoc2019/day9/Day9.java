package se.heinszn.aoc2019.day9;

import se.heinszn.aoc2019.common.IntcodeExecutorOld;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class Day9 {
    public static void main(String[] args) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1000);
        IntcodeExecutorOld executor = new IntcodeExecutorOld(PART1_TEST, System.in, outputStream);
        executor.execute();

        ByteBuffer buffer = ByteBuffer.wrap(outputStream.toByteArray());
        while (buffer.hasRemaining()) {
            System.out.print(buffer.getInt() + ", ");
        }
        System.out.println();
    }

    static int[] PART1_TEST = {
            109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99
    };

    static int[] PART1_TEST2 = {
            1102,34915192,34915192,7,4,7,99,0
    };

    static BigInteger[] PART1_TEST3 = {
            new BigInteger("104"), new BigInteger("1125899906842624"), new BigInteger("99")
    };
}
