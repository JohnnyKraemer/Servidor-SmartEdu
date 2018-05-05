package br.com.smartedu.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data @EqualsAndHashCode @ToString
public class Classify implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "period_calculation")
    private int periodCalculation;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classifier_id", referencedColumnName = "id")
    private Classifier classifier;

    @ManyToMany()
    private List<Variable> variable;
    //private int period;

}
