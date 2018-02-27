package br.com.smartedu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long codigo;
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
    private String uf_sisu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    //@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //private Set<Probability> probability;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Detail> detail;
}
