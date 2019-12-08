package se.heinszn.aoc2019.day4;

public class Day4 {

    public static void main(String[] args) {
        PasswordCracker cracker = new PasswordCracker(284639, 748759);

        long possiblePasswords = cracker.matchesRules().count();
        System.out.println("Possible passwords: " + possiblePasswords);
    }
}
