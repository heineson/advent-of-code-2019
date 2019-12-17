package se.heinszn.aoc2019.day12;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MoonSimulator {
    private final Map<Moon, Position> initialState;
    private Map<Moon, Position> moonPositions;

    private BigInteger iterations;
    private Map<Moon, BigInteger[]> periods;

    public MoonSimulator(Map<Moon, Position> initialPositions) {
        this.initialState = new HashMap<>();
        initialPositions.forEach((moon, position) -> {
            initialState.put(moon, new Position(position.getX(), position.getY(), position.getZ(),
                    position.getVx(), position.getVy(), position.getVz()));
        });
        this.moonPositions = new HashMap<>(initialPositions);

        this.iterations = BigInteger.ZERO;
        this.periods = new HashMap<>();
        initialPositions.forEach((moon, position) -> {
            periods.put(moon, new BigInteger[] { BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO });
        });
    }

    public void nextTimeStep() {
        applyGravities();
        applyVelocity();
    }

    public Map<Moon, Position> getMoonPositions() {
        return new HashMap<>(this.moonPositions);
    }

    public int calculateEnergy() {
        int sum = 0;
        for (Map.Entry<Moon, Position> entry : moonPositions.entrySet()) {
            Position p = entry.getValue();
            int pot = Math.abs(p.getX()) + Math.abs(p.getY()) + Math.abs(p.getZ());
            int kin = Math.abs(p.getVx()) + Math.abs(p.getVy()) + Math.abs(p.getVz());
            sum += pot * kin;
        }
        return sum;
    }

    public void iterationsUntilNextInitialState() {
        boolean done = false;
        while (!done) {
            nextTimeStep();
            iterations = iterations.add(BigInteger.ONE);
            checkAll();
            done = periods.values().stream()
                    .flatMap(Arrays::stream)
                    .noneMatch(BigInteger.ZERO::equals);
        }
        System.out.println("Final result:");
        periods.forEach((moon, bigIntegers) -> {
            System.out.println(moon + ": " + Arrays.toString(bigIntegers));
        });
    }
// 5780547156
// 290044118
// 130537073472
// 552708
// 5780547156 290044118 130537073472 552708 // Least common multiple of these is too big!
    private void checkAll() {
        this.moonPositions.forEach((moon, position) -> {
            Position ip = initialState.get(moon);

            // x
            if (position.getX() == ip.getX() && position.getVx() == ip.getVx()) {
                if (periods.get(moon)[0].equals(BigInteger.ZERO)) {
                    System.out.println("Found period for " + moon + " X: " + iterations);
                    periods.get(moon)[0] = this.iterations;
                }
            }

            // y
            if (position.getY() == ip.getY() && position.getVy() == ip.getVy()) {
                if (periods.get(moon)[1].equals(BigInteger.ZERO)) {
                    System.out.println("Found period for " + moon + " Y: " + iterations);
                    periods.get(moon)[1] = this.iterations;
                }
            }

            // z
            if (position.getZ() == ip.getZ() && position.getVz() == ip.getVz()) {
                if (periods.get(moon)[2].equals(BigInteger.ZERO)) {
                    System.out.println("Found period for " + moon + " Z: " + iterations);
                    periods.get(moon)[2] = this.iterations;
                }
            }


        });
    }

    private void applyGravities() {
        applyGravityForPair(moonPositions.get(Moon.I), moonPositions.get(Moon.E));
        applyGravityForPair(moonPositions.get(Moon.I), moonPositions.get(Moon.G));
        applyGravityForPair(moonPositions.get(Moon.I), moonPositions.get(Moon.C));

        applyGravityForPair(moonPositions.get(Moon.E), moonPositions.get(Moon.G));
        applyGravityForPair(moonPositions.get(Moon.E), moonPositions.get(Moon.C));

        applyGravityForPair(moonPositions.get(Moon.G), moonPositions.get(Moon.C));
    }

    private void applyGravityForPair(Position m1, Position m2) {
        if (m1.getX() > m2.getX()) {
            m1.updateVx(-1);
            m2.updateVx(1);
        } else if (m1.getX() < m2.getX()) {
            m1.updateVx(1);
            m2.updateVx(-1);
        }

        if (m1.getY() > m2.getY()) {
            m1.updateVy(-1);
            m2.updateVy(1);
        } else if (m1.getY() < m2.getY()) {
            m1.updateVy(1);
            m2.updateVy(-1);
        }

        if (m1.getZ() > m2.getZ()) {
            m1.updateVz(-1);
            m2.updateVz(1);
        } else if (m1.getZ() < m2.getZ()) {
            m1.updateVz(1);
            m2.updateVz(-1);
        }
    }

    private void applyVelocity() {
        moonPositions.forEach((moon, position) -> position.updatePositions());
    }

    enum Moon {
        I, E, G, C
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    static class Position {
        int x;
        int y;
        int z;

        int vx;
        int vy;
        int vz;

        public static Position of(int x, int y, int z) {
            return new Position(x, y, z, 0, 0, 0);
        }

        public void updateVx(int diff) {
            vx = vx + diff;
        }

        public void updateVy(int diff) {
            vy = vy + diff;
        }

        public void updateVz(int diff) {
            vz = vz + diff;
        }

        public void updatePositions() {
            x = x + vx;
            y = y + vy;
            z = z + vz;
        }
    }
}
