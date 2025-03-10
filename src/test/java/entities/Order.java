package entities;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.annotations.PrimaryKey;
import dimstyl.orm.annotations.Table;
import dimstyl.orm.annotations.UniqueConstraint;
import dimstyl.orm.marker.Entity;

import java.time.LocalDateTime;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "createdAt"}))
public class Order implements Entity {

    @PrimaryKey
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
