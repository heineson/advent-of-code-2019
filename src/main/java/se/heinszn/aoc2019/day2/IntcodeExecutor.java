package se.heinszn.aoc2019.day2;

import java.util.Arrays;
import java.util.Map;

public class IntcodeExecutor {
    public static final int OP_ADD = 1;
    public static final int OP_MULT = 2;
    public static final int OP_END = 99;

    private int[] program;
    private int pointer;

    public IntcodeExecutor(int[] program) {
        this.program = program.clone();
    }

    public int[] execute(Map<Integer, Integer> modifications) {
        modifications.forEach((key, value) -> program[key] = value);
        this.pointer = 0;

        while (executeInstruction(readOneInstruction())) {
            // nuttin'
        }

        return program.clone();
    }

    private int[] readOneInstruction() {
        int[] op = Arrays.copyOfRange(program, pointer, pointer + 4);
        pointer += 4;
        return op;
    }

    private boolean executeInstruction(int[] op) {
        switch (op[0]) {
            case OP_ADD:
                program[op[3]] = program[op[1]] + program[op[2]];
                break;
            case OP_MULT:
                program[op[3]] = program[op[1]] * program[op[2]];
                break;
            case OP_END:
                return false;
            default:
                throw new IllegalStateException("Unknown op code: " + op[0]);
        }
        return true;
    }
}
