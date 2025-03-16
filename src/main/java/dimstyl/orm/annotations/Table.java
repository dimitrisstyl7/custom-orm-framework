package dimstyl.orm.annotations;

import java.lang.annotation.*;

/**
 * Annotation to define a database table mapping for an entity.
 * <p>
 * This annotation specifies the table name and any unique constraints that should be applied
 * to the table in the database.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * Specifies the name of the table in the database.
     * <p>
     * If left empty, the class name will be used as the table name by default.
     * </p>
     *
     * @return The table name.
     */
    String name() default "";

    /**
     * Defines unique constraints applied to one or more columns of the table.
     * <p>
     * The column names must refer to the actual database column names, not entity class field names.
     * </p>
     *
     * @return An array of {@link UniqueConstraint} annotations specifying the unique constraints.
     */
    UniqueConstraint[] uniqueConstraints() default {};

}
