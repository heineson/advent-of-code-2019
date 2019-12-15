package se.heinszn.aoc2019.day11;

import se.heinszn.aoc2019.day9.Day9;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Day11 {
    public static void main(String[] args) throws URISyntaxException, IOException {
        URI resource = Day9.class.getClassLoader().getResource("day9.txt").toURI();
        PaintingRobot paintingRobot = new PaintingRobot(Path.of(resource));

        paintingRobot.run();
        System.out.println("Part 1: " + paintingRobot.getPaintedPanels().size());
    }
}
