package asgn3;

import java.util.ArrayList;
import java.util.Random;

import asgn3.Sector.SectorType;

public class Track {
    ArrayList<Sector> sectors;
    int length;

    public Track(int length) {
        this.length = length;
        this.sectors = new ArrayList<>();

        Random random = new Random();
        while (length > 0) {
            int sectorLength;
            if (length > Sector.MIN_LENGTH) {
                int bound = length / Sector.MIN_LENGTH;
                sectorLength = (random.nextInt(bound) + 1) * Sector.MIN_LENGTH;
            } else {
                sectorLength = length;
            }

            int sectorTypeRoll = random.nextInt(SectorType.values().length);
            SectorType sectorType = SectorType.values()[sectorTypeRoll];

            sectors.add(new Sector(sectorType, sectorLength));

            length -= sectorLength;
        }
    }

    public SectorType getSectorType(float pos) {
        int temp = 0;
        for (Sector sector : sectors) {
            temp += sector.length;
            if (pos < temp) {
                return sector.type;
            }
        }

        return null; // means we're off the track (i.e. finished the race)
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("|");
        for (Sector sector : sectors) {
            builder.append(sector.toString());
            builder.append("|");
        }
        return builder.toString();
    }
}
