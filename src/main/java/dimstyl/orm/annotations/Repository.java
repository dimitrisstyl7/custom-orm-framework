package dimstyl.orm.annotations;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.model.Entity;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Repository {

    String databaseName();

    DatabaseEngine databaseEngine();

    Class<? extends Entity> entity();

}
