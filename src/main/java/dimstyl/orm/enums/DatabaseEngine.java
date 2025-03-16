package dimstyl.orm.enums;

/**
 * Enum representing supported database engines.
 * <p>
 * This enum defines the database management systems that can be used within the ORM framework.
 * It provides a custom {@link #toString()} method for user-friendly names.
 * </p>
 *
 * <p><strong>Possible Values:</strong></p>
 * <ul>
 *     <li>{@link #H2} - H2 Database Engine</li>
 *     <li>{@link #SQLITE} - SQLite Database Engine</li>
 *     <li>{@link #DERBY} - Apache Derby Database Engine</li>
 * </ul>
 */
public enum DatabaseEngine {

    /**
     * H2 Database Engine.
     */
    H2,

    /**
     * SQLite Database Engine.
     */
    SQLITE,

    /**
     * Apache Derby Database Engine.
     */
    DERBY;

    /**
     * Returns a user-friendly string representation of the database engine.
     *
     * @return A formatted string representing the database engine.
     */
    @Override
    public String toString() {
        return switch (this) {
            case H2 -> "H2";
            case SQLITE -> "SQLite";
            case DERBY -> "Derby";
        };
    }

}
