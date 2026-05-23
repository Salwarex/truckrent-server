package utmn.truckrent.server;

public enum Role {
    USER(1),
    PARTNER(2),
    ADMIN(3);

    private final int level;

    Role(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
