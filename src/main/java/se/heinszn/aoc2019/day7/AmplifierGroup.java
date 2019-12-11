package se.heinszn.aoc2019.day7;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AmplifierGroup {
    private static final int SIZE = 10_000;

    private List<Amplifier> amplifiers = new ArrayList<>();
    private List<ByteBuffer> inputs = new ArrayList<>();

    public AmplifierGroup(int[] program, int[] phases) throws IOException {
        for (int phase : phases) {
            createAmplifier(program, phase);
        }
    }

    private void createAmplifier(int[] program, int phase) throws IOException {
        ByteBuffer input = ByteBuffer.allocate(4 * SIZE).putInt(phase);
        inputs.add(input);

        Amplifier amplifier = new Amplifier(program, new ByteArrayInputStream(input.array()));
        amplifiers.add(amplifier);
    }

    public int execute(int initialValue) {
        inputs.get(0).putInt(initialValue);
        int lastOutput = 0;
        while (amplifiers.stream().noneMatch(Amplifier::isFinished)) {
            for (int i=0; i<amplifiers.size(); i++) {
                Optional<Integer> output = amplifiers.get(i).runUntilOutput();
                if (output.isPresent()) {
                    lastOutput = output.get();
                    inputs.get((i + 1) % inputs.size()).putInt(output.get());
                }
            }
            System.out.println("Output after interation of all amps: " + lastOutput);
        }
        return lastOutput;
    }
}
