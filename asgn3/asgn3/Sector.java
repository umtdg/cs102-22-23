package asgn3;

public class Sector {
    public static enum SectorType {
        ASPHALT("Asphalt"),
        DIRT("Dirt"),
        STONE("Stone");

        String displayName;

        SectorType(String displayName) {
            this.displayName = displayName;
        }
    }

    static final int MIN_LENGTH = 5;

    SectorType type;
    int length;

    public Sector(SectorType type, int length) {
        this.type = type;
        this.length = length;
    }

    @Override
    public String toString() {
        return String.format("-%s %d-", this.type.displayName, this.length);
    }
}
