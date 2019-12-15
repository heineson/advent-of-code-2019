package se.heinszn.aoc2019.day10;

public class Day10 {
    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();

        AsteroidCalculator calculator = new AsteroidCalculator(DAY10_INPUT);
        AsteroidCalculator.Coord coord = calculator.calculateAll();
        System.out.println("Part 1: coord: " + coord + ", seen: " + calculator.getAsteroidsSeenFromCoord(coord));
    }

    static void test1() {
        AsteroidCalculator calculator = new AsteroidCalculator(TEST1);
        AsteroidCalculator.Coord coord = calculator.calculateAll();
        System.out.println("Test1: coord: " + coord + ", seen: " + calculator.getAsteroidsSeenFromCoord(coord));
        calculator.printMap();
    }

    static void test2() {
        AsteroidCalculator calculator = new AsteroidCalculator(TEST2);
        AsteroidCalculator.Coord coord = calculator.calculateAll();
        System.out.println("Test2: coord: " + coord + ", seen: " + calculator.getAsteroidsSeenFromCoord(coord));
    }

    static void test3() {
        AsteroidCalculator calculator = new AsteroidCalculator(TEST3);
        AsteroidCalculator.Coord coord = calculator.calculateAll();
        System.out.println("Test3: coord: " + coord + ", seen: " + calculator.getAsteroidsSeenFromCoord(coord));
    }

    static void test4() {
        AsteroidCalculator calculator = new AsteroidCalculator(TEST4);
        AsteroidCalculator.Coord coord = calculator.calculateAll();
        System.out.println("Test4: coord: " + coord + ", seen: " + calculator.getAsteroidsSeenFromCoord(coord));
    }

    static void test5() {
        AsteroidCalculator calculator = new AsteroidCalculator(TEST5);
        AsteroidCalculator.Coord coord = calculator.calculateAll();
        System.out.println("Test5: coord: " + coord + ", seen: " + calculator.getAsteroidsSeenFromCoord(coord));
    }

    static String TEST1 = ".#..#\n" +
            ".....\n" +
            "#####\n" +
            "....#\n" +
            "...##";
    static String TEST2 = "......#.#.\n" +
            "#..#.#....\n" +
            "..#######.\n" +
            ".#.#.###..\n" +
            ".#..#.....\n" +
            "..#....#.#\n" +
            "#..#....#.\n" +
            ".##.#..###\n" +
            "##...#..#.\n" +
            ".#....####";
    static String TEST3 = "#.#...#.#.\n" +
            ".###....#.\n" +
            ".#....#...\n" +
            "##.#.#.#.#\n" +
            "....#.#.#.\n" +
            ".##..###.#\n" +
            "..#...##..\n" +
            "..##....##\n" +
            "......#...\n" +
            ".####.###.";
    static String TEST4 = ".#..#..###\n" +
            "####.###.#\n" +
            "....###.#.\n" +
            "..###.##.#\n" +
            "##.##.#.#.\n" +
            "....###..#\n" +
            "..#.#..#.#\n" +
            "#..#.#.###\n" +
            ".##...##.#\n" +
            ".....#.#..";
    static String TEST5 = ".#..##.###...#######\n" +
            "##.############..##.\n" +
            ".#.######.########.#\n" +
            ".###.#######.####.#.\n" +
            "#####.##.#.##.###.##\n" +
            "..#####..#.#########\n" +
            "####################\n" +
            "#.####....###.#.#.##\n" +
            "##.#################\n" +
            "#####.##.###..####..\n" +
            "..######..##.#######\n" +
            "####.##.####...##..#\n" +
            ".#####..#.######.###\n" +
            "##...#.##########...\n" +
            "#.##########.#######\n" +
            ".####.#.###.###.#.##\n" +
            "....##.##.###..#####\n" +
            ".#.#.###########.###\n" +
            "#.#.#.#####.####.###\n" +
            "###.##.####.##.#..##";

    static String DAY10_INPUT = ".#....#.###.........#..##.###.#.....##...\n" +
            "...........##.......#.#...#...#..#....#..\n" +
            "...#....##..##.......#..........###..#...\n" +
            "....#....####......#..#.#........#.......\n" +
            "...............##..#....#...##..#...#..#.\n" +
            "..#....#....#..#.....#.#......#..#...#...\n" +
            ".....#.#....#.#...##.........#...#.......\n" +
            "#...##.#.#...#.......#....#........#.....\n" +
            "....##........#....#..........#.......#..\n" +
            "..##..........##.....#....#.........#....\n" +
            "...#..##......#..#.#.#...#...............\n" +
            "..#.##.........#...#.#.....#........#....\n" +
            "#.#.#.#......#.#...##...#.........##....#\n" +
            ".#....#..#.....#.#......##.##...#.......#\n" +
            "..#..##.....#..#.........#...##.....#..#.\n" +
            "##.#...#.#.#.#.#.#.........#..#...#.##...\n" +
            ".#.....#......##..#.#..#....#....#####...\n" +
            "........#...##...#.....#.......#....#.#.#\n" +
            "#......#..#..#.#.#....##..#......###.....\n" +
            "............#..#.#.#....#.....##..#......\n" +
            "...#.#.....#..#.......#..#.#............#\n" +
            ".#.#.....#..##.....#..#..............#...\n" +
            ".#.#....##.....#......##..#...#......#...\n" +
            ".......#..........#.###....#.#...##.#....\n" +
            ".....##.#..#.....#.#.#......#...##..#.#..\n" +
            ".#....#...#.#.#.......##.#.........#.#...\n" +
            "##.........#............#.#......#....#..\n" +
            ".#......#.............#.#......#.........\n" +
            ".......#...##........#...##......#....#..\n" +
            "#..#.....#.#...##.#.#......##...#.#..#...\n" +
            "#....##...#.#........#..........##.......\n" +
            "..#.#.....#.....###.#..#.........#......#\n" +
            "......##.#...#.#..#..#.##..............#.\n" +
            ".......##.#..#.#.............#..#.#......\n" +
            "...#....##.##..#..#..#.....#...##.#......\n" +
            "#....#..#.#....#...###...#.#.......#.....\n" +
            ".#..#...#......##.#..#..#........#....#..\n" +
            "..#.##.#...#......###.....#.#........##..\n" +
            "#.##.###.........#...##.....#..#....#.#..\n" +
            "..........#...#..##..#..##....#.........#\n" +
            "..#..#....###..........##..#...#...#..#..";
}
