package br.com.smartedu.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.swing.text.Position;
import java.io.Serializable;
import java.util.Set;

//@Entity
@Data
@EqualsAndHashCode
@ToString
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private int status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    private Position position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_id", referencedColumnName = "id")
    private Campus campus;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Course> course;
}
