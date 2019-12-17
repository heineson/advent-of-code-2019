package se.heinszn.aoc2019.common;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntcodeExecutor {
    private BigInteger[] memory;
    private int pointer;
    private int relativeBase;
    @Setter
    private boolean pauseOnOutput;
    @Getter
    private boolean paused;
    @Getter
    private boolean exited;
    @Getter
    @Setter
    private boolean readFlag;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public IntcodeExecutor(BigInteger[] program) {
        this(program, System.in, System.out);
    }

    public IntcodeExecutor(Path f, InputStream inputStream, OutputStream outputStream) throws IOException {
        this(Files.lines(f)
                    .flatMap(line -> Arrays.stream(line.split(",")))
                    .map(BigInteger::new)
                    .toArray(BigInteger[]::new),
                inputStream,
                outputStream);
    }

    public IntcodeExecutor(BigInteger[] program, InputStream inputStream, OutputStream outputStream) {
        this.memory = new BigInteger[65_536];
        Arrays.fill(this.memory, BigInteger.valueOf(0));
        System.arraycopy(program, 0, this.memory, 0, program.length);

        this.dataInputStream = new DataInputStream(inputStream);
        this.dataOutputStream = new DataOutputStream(outputStream);

        this.pauseOnOutput = false;
        this.paused = false;
        this.exited = false;
        this.readFlag = false;
    }

    public void setMemoryAddress(int pos, BigInteger value) {
        this.memory[pos] = value;
    }

    public void execute() {
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
    }

    private boolean executeNext() {
        Instruction instruction = readInstruction();
        List<Arg> args = instruction.getArgs();

        switch (instruction.getOpCode()) {
            case OP_ADD:
                memory[getWriteLocation(args.get(2))] = getArg(args.get(0)).add(getArg(args.get(1)));
                break;
            case OP_MULT:
                memory[getWriteLocation(args.get(2))] = getArg(args.get(0)).multiply(getArg(args.get(1)));
                break;
            case OP_IN:
                memory[getWriteLocation(args.get(0))] = BigInteger.valueOf(readInt());
                break;
            case OP_OUT:
                try {
                    dataOutputStream.write(getArg(args.get(0)).toByteArray());
                    if (pauseOnOutput) {
                        paused = true;
                        return false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case OP_JIT:
                if (!getArg(args.get(0)).equals(BigInteger.ZERO)) {
                    pointer = getArg(args.get(1)).intValueExact();
                }
                break;
            case OP_JIF:
                if (getArg(args.get(0)).equals(BigInteger.ZERO)) {
                    pointer = getArg(args.get(1)).intValueExact();
                }
                break;
            case OP_LT:
                memory[getWriteLocation(args.get(2))] = getArg(args.get(0)).compareTo(getArg(args.get(1))) < 0 ? BigInteger.ONE : BigInteger.ZERO;
                break;
            case OP_EQ:
                memory[getWriteLocation(args.get(2))] = getArg(args.get(0)).equals(getArg(args.get(1))) ? BigInteger.ONE : BigInteger.ZERO;
                break;
            case OP_RB:
                this.relativeBase += getArg(args.get(0)).intValueExact();
                break;
            case OP_END:
                exited = true;
                return false;
            default:
                throw new IllegalStateException("Unknown opcode: " + instruction.getOpCode());
        }

        return true;
    }

    private BigInteger getArg(Arg arg) {
        switch (arg.getMode()) {
            case I: return arg.getValue();
            case P: return memory[arg.getValue().intValueExact()];
            case R: return memory[this.relativeBase + arg.getValue().intValueExact()];
            default: throw new IllegalStateException("Unknown arg mode " + arg.getMode());
        }
    }

    private int getWriteLocation(Arg dest) {
        if (dest.getMode() == Arg.Mode.R) {
            return this.relativeBase + dest.getValue().intValueExact();
        }
        return dest.getValue().intValueExact();
    }

    private Instruction readInstruction() {
        int op = memory[pointer++].intValueExact();
        OpCode opCode = OpCode.fromInt(op % 100);
        BigInteger[] args = opCode.getParams() > 0
                ? Arrays.copyOfRange(memory, pointer, pointer + opCode.getParams())
                : new BigInteger[]{};
        pointer += opCode.getParams();

        List<Arg> argList = new ArrayList<>();
        int modes = op / 100;
        for (BigInteger arg : args) {
            argList.add(Arg.ofCode(modes % 10, arg));
            modes = modes / 10;
        }

        return Instruction.of(opCode, argList);
    }

    private int readInt() {
        try {
            readFlag = true;
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
        BigInteger value;

        static Arg ofCode(int mode, BigInteger value) {
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
