package entities;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.annotations.PrimaryKey;
import dimstyl.orm.annotations.Table;
import dimstyl.orm.model.Entity;
import lombok.ToString;

@ToString
@Table(name = "customer")
public class Customer implements Entity {

    @PrimaryKey
    @Column(nullable = false, unique = true)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private boolean active;

}
