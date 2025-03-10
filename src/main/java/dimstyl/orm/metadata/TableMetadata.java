package dimstyl.orm.metadata;

import dimstyl.orm.annotations.UniqueConstraint;

import java.util.List;

public record TableMetadata(String tableName,
                            UniqueConstraint[] uniqueConstraints,
                            List<ColumnMetadata> columnMetadataList) {

    public void addColumnMetadata(ColumnMetadata columnMetadata) {
        columnMetadataList.add(columnMetadata);
    }

}