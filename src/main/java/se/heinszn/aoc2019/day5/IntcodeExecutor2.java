package se.heinszn.aoc2019.day5;

import lombok.Value;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IntcodeExecutor2 {
    private int[] program;
    private int pointer;

    public IntcodeExecutor2(int[] program) {
        this.program = program.clone();
    }

    public int[] execute(Map<Integer, Integer> modifications) {
        modifications.forEach((key, value) -> program[key] = value);
        this.pointer = 0;

        try {
            while (executeNext()) {
                // nuttin'
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(program));
            throw new RuntimeException("Error at " + this.pointer, e);
        }

        return program.clone();
    }

    private boolean executeNext() {
        Instruction instruction = readInstruction();
        List<Arg> args = instruction.getArgs();

        switch (instruction.getOpCode()) {
            case OP_ADD:
                program[args.get(2).getValue()] = getArg(args.get(0)) + getArg(args.get(1));
                break;
            case OP_MULT:
                program[args.get(2).getValue()] = getArg(args.get(0)) * getArg(args.get(1));
                break;
            case OP_IN:
                System.out.println("Int value: ");
                program[args.get(0).getValue()] = read();
                break;
            case OP_OUT:
                System.out.println(getArg(args.get(0)));
                break;
            case OP_END:
                return false;
            default:
                throw new IllegalStateException("Unknown opcode: " + instruction.getOpCode());
        }

        return true;
    }

    private int getArg(Arg arg) {
        return arg.getMode() == Arg.Mode.I ? arg.getValue() : program[arg.getValue()];
    }

    private Instruction readInstruction() {
        int op = program[pointer++];
        OpCode opCode = OpCode.fromInt(op % 100);
        int[] args = opCode.getParams() > 0
                ? Arrays.copyOfRange(program, pointer, pointer + opCode.getParams())
                : new int[]{};
        pointer += opCode.getParams();

        List<Arg> argList = new ArrayList<>();
        int modes = op / 100;
        for (int i = 0; i < args.length; i++) {
            argList.add(Arg.ofCode(modes % 10, args[i]));
            modes = modes / 10;
        }

        return Instruction.of(opCode, argList);
    }

    private int read() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            return Integer.parseInt(br.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Value(staticConstructor = "of")
    private static class Instruction {
        private OpCode opCode;
        private List<Arg> args;
    }

    @Value(staticConstructor = "of")
    private static class Arg {
        enum Mode {P, I}

        Mode mode;
        int value;

        static Arg ofCode(int mode, int value) {
            return Arg.of(mode == 1 ? Mode.I : Mode.P, value);
        }
    }

    private enum OpCode {
        OP_ADD(1, 3),
        OP_MULT(2, 3),
        OP_IN(3, 1),
        OP_OUT(4, 1),
        OP_END(99, 0);

        private int code;
        private int params;

        OpCode(int code, int params) {
            this.code = code;
            this.params = params;
        }

        public int getCode() {
            return code;
        }

        public int getParams() {
            return params;
        }

        static OpCode fromInt(int opcode) {
            return Arrays.stream(OpCode.values())
                    .filter(o -> o.getCode() == opcode).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Illegal opcode: " + opcode));
        }
    }
}
