package br.com.smartedu.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@ToString(of = "name")
@EqualsAndHashCode
public class Variable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String name_database;
    private String table;
    private String description;
    private int useClassify;
    private int discretize;
    private int nominal;
}
