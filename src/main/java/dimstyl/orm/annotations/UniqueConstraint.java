package dimstyl.orm.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UniqueConstraint {

    String[] columnNames() default {};

}
