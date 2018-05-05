package br.com.smartedu.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@ToString
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long code;
    private String name;
    private String email;
    private String genre;
    private Integer age;
    private Double enem_human;
    private Double enem_language;
    private Double enem_math;
    private Double enem_nature;
    private Double enem_redaction;
    private Double sisu;
    private String quota;
    private Integer year_ingress;
    private Integer semester_ingress;

    @Temporal(TemporalType.DATE)
    private Date birth_date;

    private String type_ingress;
    private String country;
    private String municipality_sisu;
    private String municipality;
    private String changed_course;
    private String changed_course_campus;
    private Integer entries_other_course;
    private Integer entries_course;
    private String state;
    private String state_sisu;

   /* private Long codigo;
    private String nome;
    private String genero;
    private Integer idade_ingresso;
    private Double enem_humanas;
    private Double enem_linguagem;
    private Double enem_matematica;
    private Double enem_natureza;
    private Double enem_redacao;
    private Double nota_final_sisu;
    private String cota;
    private Integer ano_ingresso;
    private Integer semestre_ingresso;
    @Temporal(TemporalType.DATE)
    private Date data_nascimento;
    private String forma_ingresso;
    private String pais_nascimento;
    private String municipio_sisu;
    private String municipio;
    private String mudou_curso_mesmo_campus;
    private String mudou_curso_outro_campus;
    private Integer entradas_outro_curso;
    private Integer entradas_curso;
    private String uf;
    private String uf_sisu;*/

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    //@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //private Set<Probability> probability;

}
