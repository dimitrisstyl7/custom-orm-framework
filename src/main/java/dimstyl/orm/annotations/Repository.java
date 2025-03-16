package dimstyl.orm.annotations;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.model.Entity;

import java.lang.annotation.*;

/**
 * Defines a repository configuration for managing a specific entity in the database.
 * <p>
 * This annotation is used to specify the database details and the entity class
 * that the repository will manage.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Repository {

    /**
     * Specifies the name of the database where the entity is stored.
     *
     * @return The database name.
     */
    String databaseName();

    /**
     * Defines the database engine used for the repository.
     *
     * @return The {@link DatabaseEngine} used by the repository.
     */
    DatabaseEngine databaseEngine();

    /**
     * Specifies the entity class managed by this repository.
     * <p>
     * The entity must extend {@link Entity} to be recognized as a valid database table.
     * </p>
     *
     * @return The entity class associated with this repository.
     */
    Class<? extends Entity> entity();

}
