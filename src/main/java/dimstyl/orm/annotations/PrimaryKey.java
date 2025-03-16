package dimstyl.orm.annotations;

import java.lang.annotation.*;

/**
 * Annotation to specify the primary key field of an entity.
 * <p>
 * This annotation marks a field as the primary key, which uniquely identifies an entity in the database.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {
}
