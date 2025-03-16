package dimstyl.orm.annotations;

import java.lang.annotation.*;

/**
 * Annotation to define a unique constraint on one or more database columns.
 * <p>
 * This annotation ensures that the specified columns have unique values in the database.
 * The column names should correspond to the actual column names in the database, not the entity class field names.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UniqueConstraint {

    /**
     * Specifies the names of the database columns that should have unique values.
     *
     * @return An array of column names enforcing uniqueness.
     */
    String[] columnNames() default {};

}
