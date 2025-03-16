package dimstyl.orm.metadata;

import dimstyl.orm.enums.DatabaseEngine;
import dimstyl.orm.enums.SqlOperation;

import java.util.List;

public record DatabaseMetadata(String databaseName,
                               DatabaseEngine databaseEngine,
                               SqlOperation sqlOperation,
                               List<TableMetadata> tableMetadataList) implements Metadata {

    public void addTableMetadata(TableMetadata tableMetadata) {
        tableMetadataList.add(tableMetadata);
    }

}