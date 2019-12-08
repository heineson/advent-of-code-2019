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
        String asString = String.valueOf(number);

        for (int i = 0; i < asString.length() - 1; i++) {
            if (asString.charAt(i) > asString.charAt(i + 1)) {
                return false;
            }
        }

        while (asString.length() > 1) {
            char c = asString.charAt(0);
            int pointer = 1;
            int count = 1;

            while (c == asString.charAt(pointer)) {
                count++;
                if (++pointer >= asString.length()) {
                    break;
                }
            }

            if (count == 2) {
                return true;
            }
            asString = asString.substring(Math.max(1, count - 1));
        }

        return false;
    }
}
