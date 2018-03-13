package br.com.smartedu.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@ToString
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nivel_ensino;
    private String grau;
    private String periodicidade;
    private String funcionamento;
    private String turno;
    private String categoria_stricto_sensu;
    private String codigo_curso;
    private String codigo_inep_curso;
    private String regime_ensino;
    private Integer total_periodos;
    
    private int useClassify;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_id", referencedColumnName = "id")
    private Campus campus;
    
    @ManyToMany()
    private List<Classify> classify;
}
