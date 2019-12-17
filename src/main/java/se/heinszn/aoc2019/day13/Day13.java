package se.heinszn.aoc2019.day13;

import java.net.URI;
import java.nio.file.Path;

public class Day13 {
    public static void main(String[] args) throws Exception {
        URI resource = Day13.class.getClassLoader().getResource("day13.txt").toURI();

        ArcadeGamePlayer player = new ArcadeGamePlayer(Path.of(resource));
        player.run(0);
        System.out.println("Part 1: " + player.getScreen().values().stream().filter(ArcadeGamePlayer.TileType.BLOCK::equals).count());

        player = new ArcadeGamePlayer(Path.of(resource));
        player.run(2);
        System.out.println("Part 2: " + player.getScore());
    }
}
