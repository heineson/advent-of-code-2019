package se.heinszn.aoc2019.day14;

import lombok.Getter;
import lombok.Value;

import java.util.*;
import java.util.stream.Collectors;

public class Factory {

    List<Formula> formulas;
    @Getter
    Map<String, Long> leftoverAmounts;
    @Getter
    long ore;

    public Factory(List<String> stringFormulas) {
        this(stringFormulas, new HashMap<>());
    }

    public Factory(List<String> stringFormulas, Map<String, Long> initialLeftovers) {
        formulas = stringFormulas.stream().map(Formula::ofString).collect(Collectors.toList());
        leftoverAmounts = new HashMap<>(initialLeftovers);
        ore = 0;
    }

    public long getOreForOneFuel() {
        Formula fuelFormula = getFormulaForSubstance("FUEL").get();
        List<List<ReactionPart>> parts = acquireSources(List.of(fuelFormula.getInputs()));

        while (parts.size() > 0) {
            parts = acquireSources(parts);
        }

        return ore;
    }

    private List<List<ReactionPart>> acquireSources(List<List<ReactionPart>> wantedSubstances) {
        Map<String, Integer> amounts = new HashMap<>();
        wantedSubstances.stream().flatMap(Collection::stream).collect(Collectors.toList()).forEach(rp -> {
            amounts.put(rp.getSubstance(), amounts.getOrDefault(rp.getSubstance(), 0) + rp.getAmount());
        });

        List<List<ReactionPart>> newRequirements = new ArrayList<>();
        amounts.forEach((s, amount) -> {
            if (amount < leftoverAmounts.getOrDefault(s, 0L)) {
                leftoverAmounts.put(s, leftoverAmounts.get(s) - amount);
            } else {
                Optional<Formula> maybeFormula = getFormulaForSubstance(s);
                if (maybeFormula.isPresent()) {
                    if ("ORE".equals(s)) {
                        throw new IllegalStateException("Did not expect ORE");
                    }
                    Formula f = maybeFormula.get();

                    // Get needed amount
                    while (leftoverAmounts.getOrDefault(s, 0L) < amount) {
                        //System.out.println("Running formula: " + f);
                        newRequirements.add(f.getInputs());
                        leftoverAmounts.put(f.getOutput().getSubstance(), leftoverAmounts.getOrDefault(s, 0L) + f.getOutput().getAmount());
                    }
                    // Use needed amount
                    leftoverAmounts.put(s, leftoverAmounts.get(s) - amount);
                } else {
                    if (!"ORE".equals(s)) {
                        throw new IllegalStateException("Expected ORE, got: " + s);
                    }
                    ore += amount;
                }
            }
        });

        return newRequirements;
    }

    private Optional<Formula> getFormulaForSubstance(String s) {
        return formulas.stream().filter(f -> s.equals(f.getOutput().getSubstance())).findFirst();
    }

    @Value
    static class Formula {
        List<ReactionPart> inputs;
        ReactionPart output;

        static Formula ofString(String f) {
            String[] fp = f.split("=>");

            List<ReactionPart> inputs = Arrays.stream(fp[0].split(","))
                    .map(ReactionPart::ofString)
                    .collect(Collectors.toList());

            return new Formula(inputs, ReactionPart.ofString(fp[1]));
        }
    }

    @Value
    static class ReactionPart {
        String substance;
        int amount;

        static ReactionPart ofString(String rp) {
            String[] s = rp.trim().split(" ");
            return new ReactionPart(s[1].trim(), Integer.parseInt(s[0].trim()));
        }
    }
}
