package dimstyl.orm.annotations;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.model.Entity;

import java.lang.annotation.*;

/**
 * Defines a database configuration for the custom ORM framework.
 * <p>
 * This annotation is used to specify the database name, engine type, and the entities (tables)
 * that belong to this database.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Database {

    /**
     * Specifies the name of the database.
     * <p>
     * If left empty, the interface's name will be used as the database name by default.
     * </p>
     *
     * @return The name of the database.
     */
    String name() default "";

    /**
     * Defines the database engine to be used.
     * <p>
     * The database engine determines how data is stored and managed.
     * </p>
     *
     * @return The {@link DatabaseEngine} used by the database.
     */
    DatabaseEngine engine();

    /**
     * Specifies the entity classes that represent tables in this database.
     * <p>
     * Each class should extend {@link Entity} to be recognized as a table.
     * </p>
     *
     * @return An array of entity classes representing database tables.
     */
    Class<? extends Entity>[] tables() default {};

}
