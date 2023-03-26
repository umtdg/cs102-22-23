package asgn3;

import asgn3.Sector.SectorType;

public class Quadruped extends Vehicle {
    public Quadruped(String name) {
        this(name, 0.0f, 0);
    }

    Quadruped(String name, float speed, int fuelCapacity) {
        super(name, speed, fuelCapacity);

        this.speedFactors.put(SectorType.ASPHALT, 0.5f);
        this.speedFactors.put(SectorType.DIRT, 1.0f);
        this.speedFactors.put(SectorType.STONE, 0.75f);

        this.speedRange[0] = 20;
        this.speedRange[1] = 40;

        this.fuelRange[0] = 10;
        this.fuelRange[1] = 20;
    }
}
