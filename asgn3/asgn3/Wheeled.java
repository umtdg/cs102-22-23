package asgn3;

import asgn3.Sector.SectorType;

public class Wheeled extends Vehicle {
    public Wheeled(String name) {
        this(name, 0.0f, 0);
    }

    public Wheeled(String name, float speed, int fuelCapacity) {
        super(name, speed, fuelCapacity);

        this.speedFactors.put(SectorType.ASPHALT, 1.0f);
        this.speedFactors.put(SectorType.DIRT, 0.75f);
        this.speedFactors.put(SectorType.STONE, 0.75f);

        this.speedRange[0] = 15;
        this.speedRange[1] = 25;

        this.fuelRange[0] = 30;
        this.fuelRange[1] = 40;
    }
}
