package dimstyl.orm.metadata;

import dimstyl.orm.enums.DatabaseType;

import java.util.List;

public record DatabaseMetadata(String databaseName,
                               DatabaseType databaseType,
                               List<TableMetadata> tableMetadataList) {

    public void addTableMetadata(TableMetadata tableMetadata) {
        tableMetadataList.add(tableMetadata);
    }

}