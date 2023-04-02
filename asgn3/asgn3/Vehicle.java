package asgn3;

import java.util.HashMap;

import asgn3.Sector.SectorType;

public abstract class Vehicle {
    // Constants, differ from vehicle to vehicle
    final HashMap<Sector.SectorType, Float> speedFactors = new HashMap<Sector.SectorType, Float>();
    final float[] speedRange = new float[2];
    final int[] fuelRange = new int[2];

    // Fields
    String name;
    float speed;
    float pos;
    int fuelCapacity;
    int fuel;

    // Constructors
    Vehicle(String name) {
        this(name, 0.0f, 0);
    }

    Vehicle(String name, float speed, int fuelCapacity) {
        this.name = name;
        this.speed = speed;
        this.fuel = fuelCapacity;
        this.fuelCapacity = fuelCapacity;
        this.pos = 0;
    }

    // Getters
    public String getName() { return name; }
    public float getSpeed() { return speed; }
    public float getPos() { return pos; }
    public int getFuel() { return fuel; }

    public float getSpeedFactor(SectorType roadType) {
        return this.speedFactors.get(roadType);
    }


    // Move the vehicle if there is fuel
    public float move(SectorType roadType) {
        if (this.fuel <= 0) return 0.0f;

        float distance = this.speed * this.getSpeedFactor(roadType);

        this.pos += distance;
        this.fuel--;

        return distance;
    }

    // Reset position to 0 and refuel keeping speed and name.
    public void reset() {
        this.pos = 0;
        this.fuel = fuelCapacity;
    }

    public String display(boolean displayPos) {
        StringBuilder fmtBuilder = new StringBuilder("%s");
        if (displayPos) {
            fmtBuilder.append(" - Position: %.2f");
        }
        fmtBuilder.append(" - Speed: %.2f");
        fmtBuilder.append(" - Fuel: %d");

        if (displayPos) {
            return String.format(
                fmtBuilder.toString(),
                name, pos, speed, fuel
            );
        } else {
            return String.format(
                fmtBuilder.toString(),
                name, speed, fuel
            );
        }
    }
}
