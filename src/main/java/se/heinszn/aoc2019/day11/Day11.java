package se.heinszn.aoc2019.day11;

import se.heinszn.aoc2019.day9.Day9;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Day11 {
    public static void main(String[] args) throws URISyntaxException, IOException {
        URI resource = Day9.class.getClassLoader().getResource("day11.txt").toURI();

        PaintingRobot paintingRobot = new PaintingRobot(Path.of(resource), null);
        paintingRobot.run();
        System.out.println("Part 1: " + paintingRobot.getPaintedPanels().size());

        PaintingRobot paintingRobot2 = new PaintingRobot(Path.of(resource), PaintingRobot.WHITE);
        paintingRobot2.run();
        System.out.println("Part 2: " + paintingRobot2.getPaintedPanels().size());
        paintingRobot2.printImage();
    }
}
