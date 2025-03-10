package entities;

import dimstyl.orm.annotations.Column;
import dimstyl.orm.annotations.PrimaryKey;
import dimstyl.orm.annotations.Table;
import dimstyl.orm.marker.Entity;

import java.util.Date;

@Table(name = "user")
public class User implements Entity {

    @PrimaryKey
    @Column(nullable = false, unique = true)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

}
