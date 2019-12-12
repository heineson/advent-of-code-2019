package se.heinszn.aoc2019.common;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IntcodeExecutorOld {
    private int[] memory;
    private int pointer;
    private int relativeBase;
    @Setter
    private boolean pauseOnOutput;
    @Getter
    private boolean paused;
    @Getter
    private boolean exited;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public IntcodeExecutorOld(int[] program) {
        this(program, System.in, System.out);
    }

    public IntcodeExecutorOld(int[] program, InputStream inputStream, OutputStream outputStream) {
        this.memory = new int[65_536];
        Arrays.fill(this.memory, 0);
        System.arraycopy(program, 0, this.memory, 0, program.length);

        this.dataInputStream = new DataInputStream(inputStream);
        this.dataOutputStream = new DataOutputStream(outputStream);

        this.pauseOnOutput = false;
        this.paused = false;
        this.exited = false;
    }

    public int[] execute() {
        return execute(Map.of());
    }

    public int[] execute(Map<Integer, Integer> modifications) {
        modifications.forEach((key, value) -> memory[key] = value);
        if (!paused) {
            this.pointer = 0;
            this.relativeBase = 0;
        } else {
            paused = false;
        }
        exited = false;

        try {
            while (executeNext()) {
                // nuttin'
            }
        } catch (Exception e) {
            System.err.println(Arrays.toString(memory));
            throw new RuntimeException("Error at " + this.pointer, e);
        }

        return memory.clone();
    }

    private boolean executeNext() {
        Instruction instruction = readInstruction();
        List<Arg> args = instruction.getArgs();

        switch (instruction.getOpCode()) {
            case OP_ADD:
                memory[args.get(2).getValue()] = getArg(args.get(0)) + getArg(args.get(1));
                break;
            case OP_MULT:
                memory[args.get(2).getValue()] = getArg(args.get(0)) * getArg(args.get(1));
                break;
            case OP_IN:
                memory[args.get(0).getValue()] = read();
                break;
            case OP_OUT:
                try {
                    dataOutputStream.writeInt(getArg(args.get(0)));
                    if (pauseOnOutput) {
                        paused = true;
                        return false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case OP_JIT:
                if (getArg(args.get(0)) != 0) {
                    pointer = getArg(args.get(1));
                }
                break;
            case OP_JIF:
                if (getArg(args.get(0)) == 0) {
                    pointer = getArg(args.get(1));
                }
                break;
            case OP_LT:
                memory[args.get(2).getValue()] = getArg(args.get(0)) < getArg(args.get(1)) ? 1 : 0;
                break;
            case OP_EQ:
                memory[args.get(2).getValue()] = getArg(args.get(0)) == getArg(args.get(1)) ? 1 : 0;
                break;
            case OP_RB:
                this.relativeBase += getArg(args.get(0));
                break;
            case OP_END:
                exited = true;
                return false;
            default:
                throw new IllegalStateException("Unknown opcode: " + instruction.getOpCode());
        }

        return true;
    }

    private int getArg(Arg arg) {
        switch (arg.getMode()) {
            case I: return arg.getValue();
            case P: return memory[arg.getValue()];
            case R: return memory[this.relativeBase + arg.getValue()];
            default: throw new IllegalStateException("Unknown arg mode " + arg.getMode());
        }
    }

    private Instruction readInstruction() {
        int op = memory[pointer++];
        OpCode opCode = OpCode.fromInt(op % 100);
        int[] args = opCode.getParams() > 0
                ? Arrays.copyOfRange(memory, pointer, pointer + opCode.getParams())
                : new int[]{};
        pointer += opCode.getParams();

        List<Arg> argList = new ArrayList<>();
        int modes = op / 100;
        for (int arg : args) {
            argList.add(Arg.ofCode(modes % 10, arg));
            modes = modes / 10;
        }

        return Instruction.of(opCode, argList);
    }

    private int read() {
        try {
            return dataInputStream.readInt();
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
        enum Mode {P, I, R}

        Mode mode;
        int value;

        static Arg ofCode(int mode, int value) {
            switch (mode) {
                case 0: return Arg.of(Mode.P, value);
                case 1: return Arg.of(Mode.I, value);
                case 2: return Arg.of(Mode.R, value);
                default: throw new IllegalArgumentException("Unknown argument mode: " + mode);
            }
        }
    }

    private enum OpCode {
        OP_ADD(1, 3),
        OP_MULT(2, 3),
        OP_IN(3, 1),
        OP_OUT(4, 1),
        OP_JIT(5, 2),
        OP_JIF(6, 2),
        OP_LT(7, 3),
        OP_EQ(8, 3),
        OP_RB(9, 1),
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
