package dimstyl.orm.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    String name() default "";

    UniqueConstraint[] uniqueConstraints() default {};

}
