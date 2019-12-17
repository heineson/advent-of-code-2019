package se.heinszn.aoc2019.day12;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

public class MoonSimulator {
    private Map<Moon, Position> moonPositions;

    public MoonSimulator(Map<Moon, Position> initialPositions) {
        this.moonPositions = new HashMap<>(initialPositions);
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
