package dimstyl.orm.resolvers;

import dimstyl.orm.exceptions.UnsupportedFieldTypeException;

import java.lang.reflect.Field;

public interface ColumnTypeResolver {

    String resolve(final Field field) throws UnsupportedFieldTypeException;

}
