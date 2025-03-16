package dimstyl.orm.annotations;

import java.lang.annotation.*;

/**
 * Annotation to mark a method for selecting all records from an entity table.
 * <p>
 * This annotation is used in repository interfaces to specify that a method
 * should retrieve all records from the corresponding entity's table.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SelectAll {
}
