package se.heinszn.aoc2019.day14;

import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Factory {

    List<Formula> formulas;
    Map<String, Integer> substanceAmounts;

    public Factory(List<String> stringFormulas) {
        formulas = stringFormulas.stream().map(Formula::ofString).collect(Collectors.toList());
        substanceAmounts = new HashMap<>();
    }

    public int getOreForOneFuel() {
        Formula fuelFormula = getFormulaForSubstance("FUEL");

        Map<String, Integer> sources;
        do {
            sources = new HashMap<>();
            sources = getSources(List.of(fuelFormula.getInputs()));
        } while (sources.keySet())
    }

    private Map<String, Integer> getSources(List<List<ReactionPart>> inputs) {
        Map<String, Integer> amounts = new HashMap<>();
        inputs.stream().flatMap(Collection::stream).collect(Collectors.toList()).forEach(rp -> {
            amounts.put(rp.getSubstance(), amounts.getOrDefault(rp.getSubstance(), 0) + rp.getAmount());
        });
        return amounts;
    }

    private Formula getFormulaForSubstance(String s) {
        return formulas.stream().filter(f -> s.equals(f.getOutput().getSubstance())).findFirst().get();
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
