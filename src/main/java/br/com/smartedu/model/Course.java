package br.com.smartedu.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
