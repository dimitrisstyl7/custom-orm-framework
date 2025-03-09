package dimstyl.orm.annotations;

import dimstyl.orm.enums.DatabaseType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Database {

    String name() default "";

    DatabaseType type();

}
