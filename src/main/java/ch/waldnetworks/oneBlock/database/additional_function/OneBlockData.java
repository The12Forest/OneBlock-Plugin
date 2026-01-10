package ch.waldnetworks.oneBlock.database.additional_function;

public class OneBlockData {
    private final String world;
    private final int x;
    private final int y;
    private final int z;
    private final int ID;
    private final int level;
    private final int progress;

    public OneBlockData(String world, int x, int y, int z, int ID, int level, int progress) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.ID = ID;
        this.level = level;
        this.progress = progress;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getID() {
        return ID;
    }

    public int getLevel() {
        return level;
    }

    public int getProgress() {
        return progress;
    }
}

