package entities;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.annotations.PrimaryKey;
import dimstyl.orm.annotations.Table;
import dimstyl.orm.annotations.UniqueConstraint;
import dimstyl.orm.model.Entity;
import lombok.ToString;

@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "created_at"}))
public class Purchase implements Entity {

    @PrimaryKey
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private Integer customerId;

    @Column(nullable = false)
    private String createdAt;

}
