package asgn3;

import asgn3.Sector.SectorType;

public class Flying extends Vehicle {
    public Flying(String name) {
        this(name, 0.0f, 0);
    }

    Flying(String name, float speed, int fuelCapacity) {
        super(name, speed, fuelCapacity);

        this.speedFactors.put(SectorType.ASPHALT, 1.0f);
        this.speedFactors.put(SectorType.DIRT, 1.0f);
        this.speedFactors.put(SectorType.STONE, 1.0f);

        this.speedRange[0] = 20;
        this.speedRange[1] = 30;

        this.fuelRange[0] = 20;
        this.fuelRange[1] = 30;
    }
}
