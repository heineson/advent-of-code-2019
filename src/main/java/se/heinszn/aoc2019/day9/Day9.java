package se.heinszn.aoc2019.day9;

import se.heinszn.aoc2019.common.IntcodeExecutor;
import se.heinszn.aoc2019.common.IntcodeExecutorOld;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day9 {
    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();

        URI resource = Day9.class.getClassLoader().getResource("day9.txt").toURI();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1000);
        IntcodeExecutor executor = new IntcodeExecutor(
                Path.of(resource),
                new ByteArrayInputStream(ByteBuffer.allocate(4).putInt(1).array()),
                outputStream);
        executor.execute();
        System.out.println("Part 1: " + new BigInteger(outputStream.toByteArray()));
    }

    private static void test1() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1000);
        IntcodeExecutor executor = new IntcodeExecutor(PART1_TEST, System.in, outputStream);
        executor.execute();

        System.out.println("Test 1");
        System.out.println("Expected: " + Arrays.stream(PART1_TEST).map(BigInteger::toByteArray).map(Arrays::toString).collect(Collectors.toList()));
        System.out.println("Actual  : " + Arrays.toString(outputStream.toByteArray()));
    }

    private static void test2() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1000);
        IntcodeExecutor executor = new IntcodeExecutor(PART1_TEST2, System.in, outputStream);
        executor.execute();

        System.out.println("Test 2: " + new BigInteger(outputStream.toByteArray()));
    }

    private static void test3() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1000);
        IntcodeExecutor executor = new IntcodeExecutor(PART1_TEST3, System.in, outputStream);
        executor.execute();

        System.out.println("Test 3: " + new BigInteger(outputStream.toByteArray()));
    }

    static BigInteger[] PART1_TEST = {
            BigInteger.valueOf(109),BigInteger.valueOf(1),BigInteger.valueOf(204),BigInteger.valueOf(-1),BigInteger.valueOf(1001),
            BigInteger.valueOf(100),BigInteger.valueOf(1),BigInteger.valueOf(100),BigInteger.valueOf(1008),BigInteger.valueOf(100),
            BigInteger.valueOf(16),BigInteger.valueOf(101),BigInteger.valueOf(1006),BigInteger.valueOf(101),BigInteger.valueOf(0),BigInteger.valueOf(99)
    };

    static BigInteger[] PART1_TEST2 = {
            BigInteger.valueOf(1102),BigInteger.valueOf(34915192),BigInteger.valueOf(34915192),BigInteger.valueOf(7),BigInteger.valueOf(4),BigInteger.valueOf(7),BigInteger.valueOf(99),BigInteger.valueOf(0)
    };

    static BigInteger[] PART1_TEST3 = {
            new BigInteger("104"), new BigInteger("1125899906842624"), new BigInteger("99")
    };
}
