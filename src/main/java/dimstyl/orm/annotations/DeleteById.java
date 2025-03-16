package dimstyl.orm.annotations;

import java.lang.annotation.*;

/**
 * Annotation to mark a method for deleting an entity by its identifier.
 * <p>
 * This annotation is used in repository interfaces to specify that a method
 * should execute a delete operation based on the entity's primary key.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DeleteById {
}
