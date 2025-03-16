package dimstyl.orm.annotations;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.model.Entity;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Database {

    String name() default "";

    DatabaseEngine engine();

    Class<? extends Entity>[] tables() default {};

}
