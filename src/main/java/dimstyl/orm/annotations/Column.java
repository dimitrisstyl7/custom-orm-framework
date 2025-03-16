package dimstyl.orm.annotations;

import java.lang.annotation.*;

/**
 * Represents a database column mapping for an entity field in the custom ORM framework.
 * <p>
 * This annotation is used to specify metadata for a field that corresponds to a database column,
 * such as its name, nullability, and uniqueness constraints.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * Specifies the name of the database column.
     * <p>
     * If left empty, the field's name will be used as the column name by default.
     * </p>
     *
     * @return The name of the column.
     */
    String name() default "";

    /**
     * Indicates whether the column can have null values.
     * <p>
     * Defaults to {@code true}, meaning the column allows null values unless explicitly set to {@code false}.
     * </p>
     *
     * @return {@code true} if the column allows null values, otherwise {@code false}.
     */
    boolean nullable() default true;

    /**
     * Specifies whether the column should have a unique constraint.
     * <p>
     * Defaults to {@code false}, meaning the column is not unique unless explicitly set to {@code true}.
     * </p>
     *
     * @return {@code true} if the column must have unique values, otherwise {@code false}.
     */
    boolean unique() default false;

}
