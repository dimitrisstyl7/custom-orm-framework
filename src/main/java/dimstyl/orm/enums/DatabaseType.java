package dimstyl.orm.enums;

public enum DatabaseType {

    H2, SQLITE, DERBY;

    @Override
    public String toString() {
        return switch (this) {
            case H2 -> "H2";
            case SQLITE -> "SQLite";
            case DERBY -> "Derby";
        };
    }

}
