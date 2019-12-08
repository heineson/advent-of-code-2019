package se.heinszn.aoc2019.day4;

import java.util.stream.IntStream;

public class PasswordCracker {
    private int startInclusive;
    private int endInclusive;

    public PasswordCracker(int startInclusive, int endInclusive) {
        this.startInclusive = startInclusive;
        this.endInclusive = endInclusive;
    }

    public IntStream matchesRules() {
        return IntStream.rangeClosed(startInclusive, endInclusive).filter(this::matchesRules);
    }

    private boolean matchesRules(int number) {
        boolean adjacentEquals = false;
        boolean isOnlyIncreasing = true;
        String asString = String.valueOf(number);
        for (int i = 0; i < asString.length() - 1; i++) {
            if (asString.charAt(i) > asString.charAt(i + 1)) {
                isOnlyIncreasing = false;
                break;
            }
            if (!adjacentEquals && asString.charAt(i) == asString.charAt(i + 1)) {
                adjacentEquals = true;
            }
        }
        return adjacentEquals && isOnlyIncreasing;
    }
}
