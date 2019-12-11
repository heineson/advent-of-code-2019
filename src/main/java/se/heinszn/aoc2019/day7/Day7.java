package se.heinszn.aoc2019.day7;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Day7 {
    public static void main(String[] args) throws IOException {
        //System.out.println("Result: " + runOnce(new int[] { 1,0,4,3,2 }));
        int max = 0;
        Set<Integer> values = Set.of(0, 1, 2, 3, 4);
        for (int a : values) {
            for (int b : remaining(values, Set.of(a))) {
                for (int c : remaining(values, Set.of(a, b))) {
                    for (int d : remaining(values, Set.of(a, b, c))) {
                        for (int e : remaining(values, Set.of(a, b, c, d))) {
                            max = Math.max(max, runOnce(new int[] { a, b, c, d, e }));
                        }
                    }
                }
            }
        }

        System.out.println("Max: " + max);
    }

    private static Set<Integer> remaining(Set<Integer> values, Set<Integer> used) {
        Set<Integer> integers = new HashSet<>(values);
        integers.removeAll(used);
        return integers;
    }

    private static int runOnce(int[] phases) throws IOException {
        int out1 = runAmplifier(new int[]{phases[0], 0});
        int out2 = runAmplifier(new int[]{phases[1], out1});
        int out3 = runAmplifier(new int[]{phases[2], out2});
        int out4 = runAmplifier(new int[]{phases[3], out3});
        return runAmplifier(new int[]{phases[4], out4});
    }

    private static int runAmplifier(int[] input) throws IOException {
        Amplifier A = new Amplifier(DAY7_PROGRAM, input);
        return A.amplify();
    }

    static final int[] TEST_PROGRAM = {
            3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0
    };

    static final int[] DAY7_PROGRAM = {
            3,8,1001,8,10,8,105,1,0,0,21,46,55,68,89,110,191,272,353,434,99999,3,9,1002,9,3,9,1001,9,3,9,102,4,9,9,101,4,9,9,1002,9,5,9,4,9,99,3,9,102,3,9,9,4,9,99,3,9,1001,9,5,9,102,4,9,9,4,9,99,3,9,1001,9,5,9,1002,9,2,9,1001,9,5,9,1002,9,3,9,4,9,99,3,9,101,3,9,9,102,3,9,9,101,3,9,9,1002,9,4,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,99,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,99,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,99
    };
}
