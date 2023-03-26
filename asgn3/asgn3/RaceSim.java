package asgn3;

import java.util.*;

import asgn3.Sector.SectorType;

public class RaceSim {
    final Random random = new Random();

    Vehicle[] vehicles;
    Track track;

    public RaceSim(int numVehicles, int trackLength) {
        vehicles = new Vehicle[numVehicles];
        generateVehicles(.4f, .25f);

        track = new Track(trackLength);
        System.out.printf("The following track is generated:\n%s\n\n", track.toString());

        System.out.println("The following vehicles are generated:");
        printVehicles(false);
    }

    // Create vehicles given probabilities
    private void generateVehicles(float wheeledProb, float flyingProb) {
        int wheeledCount = 0;
        int flyingCount = 0;
        int quadrupedCount = 0;

        for (int i = 0; i < vehicles.length; i++) {
            Vehicle vehicle;
            float vehicleRoll = random.nextFloat();

            if (vehicleRoll < wheeledProb) {
                vehicle = new Wheeled("W" + ++wheeledCount);
            } else if (vehicleRoll < wheeledProb + flyingProb) {
                vehicle = new Flying("F" + ++flyingCount);
            } else {
                vehicle = new Quadruped("Q" + ++quadrupedCount);
            }

            // Set the speed to a random value within the range
            vehicle.speed =
                random.nextFloat() * (vehicle.speedRange[1] - vehicle.speedRange[0]) + vehicle.speedRange[0];
            vehicle.fuelCapacity =
                random.nextInt(vehicle.fuelRange[1] - vehicle.fuelRange[0] + 1) + vehicle.fuelRange[0];
            vehicle.fuel = vehicle.fuelCapacity;

            vehicles[i] = vehicle;
        }
    }

    // Print a list of vehicles
    private void printVehicles(boolean printPos) {
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.display(printPos));
        }
    }

    // Get the vehicle with the highest position
    private Vehicle getFirstPosition() {
        Vehicle firstPos = vehicles[0];
        for (int i = 1; i < vehicles.length; i++) {
            if (vehicles[i].pos > firstPos.pos) {
                firstPos = vehicles[i];
            }
        }

        return firstPos;
    }

    public void simulate() {
        int turn = 1;
        Vehicle winner = null;

        boolean hasVehicleWithFuel;
        do {
            hasVehicleWithFuel = false;

            System.out.println();
            System.out.printf("Turn %d:\n", turn);
            printVehicles(true);

            System.out.println();
            System.out.println("Movements:");
            for (Vehicle vehicle : vehicles) {
                // get sector type and speed factor
                SectorType sectorType = track.getSectorType(vehicle.pos);
                float speedFactor = vehicle.getSpeedFactor(sectorType);

                // move the vehicle
                float distance = vehicle.move(sectorType);

                // print the vehicle's movement
                if (distance > 0) {
                    System.out.printf(
                        "%s moves from %s, for %.2f * %.2f = %.2f units\n",
                        vehicle.name, sectorType.displayName,
                        vehicle.speed, speedFactor, distance
                    );
                } else {
                    System.out.printf("%s has no fuel left\n", vehicle.name);
                }

                if (vehicle.fuel > 0) {
                    hasVehicleWithFuel = true;
                }

                if (vehicle.pos >= track.length) {
                    winner = vehicle;
                    break;
                }
            }

            turn++;
        } while (hasVehicleWithFuel && winner == null);

        System.out.println();

        if (winner != null) {
            System.out.printf(
                "%s finishes the race! Position: %.2f, Speed: %.2f, Fuel: %d\n",
                winner.name, winner.pos, winner.speed, winner.fuel
            );
        } else {
            winner = getFirstPosition();

            System.out.println("All vehicles have run out of fuel!");
            System.out.printf(
                "%s is at the first position! Position: %.2f, Speed: %.2f, Fuel: %d\n",
                winner.name, winner.pos, winner.speed, winner.fuel
            );
        }
    }
}
