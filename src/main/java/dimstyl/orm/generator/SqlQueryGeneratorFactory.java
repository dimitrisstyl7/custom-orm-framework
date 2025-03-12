package dimstyl.orm.generator;

public final class SqlQueryGeneratorFactory {

    private SqlQueryGeneratorFactory() {
    }

    public static SqlQueryGenerator getGenerator() {
        return GenericSqlQueryGenerator.INSTANCE;
    }

}
