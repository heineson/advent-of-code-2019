package se.heinszn.aoc2019.day2;

import se.heinszn.aoc2019.common.IntcodeExecutorOld;

import java.util.Map;

public class Day2 {

    public static void main(String[] args) {
        System.out.println("Part 1 answer: " + runProgram(12, 2));

        int[] result = findNounAndVerbForOutput(19690720);
        System.out.println("Part 2 answer: " + (100 * result[0] + result[1]));
    }

    private static int[] findNounAndVerbForOutput(int output) {
        for (int noun = 0; noun <= 99; noun ++) {
            for (int verb = 0; verb <= 99; verb++) {
                if (runProgram(noun, verb) == output) {
                    return new int[] {noun, verb};
                }
            }
        }
        return null;
    }

    private static int runProgram(int noun, int verb) {
        IntcodeExecutorOld executor = new IntcodeExecutorOld(DAY2_PROGRAM);
        return executor.execute(Map.of(1, noun, 2, verb))[0];
    }

    public static final int[] DAY2_PROGRAM = new int[] {
            1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,10,19,1,19,5,23,2,23,9,27,1,5,27,31,1,9,31,35,1,35,10,39,2,13,39,43,1,43,9,47,1,47,9,51,1,6,51,55,1,13,55,59,1,59,13,63,1,13,63,67,1,6,67,71,1,71,13,75,2,10,75,79,1,13,79,83,1,83,10,87,2,9,87,91,1,6,91,95,1,9,95,99,2,99,10,103,1,103,5,107,2,6,107,111,1,111,6,115,1,9,115,119,1,9,119,123,2,10,123,127,1,127,5,131,2,6,131,135,1,135,5,139,1,9,139,143,2,143,13,147,1,9,147,151,1,151,2,155,1,9,155,0,99,2,0,14,0
    };
}
