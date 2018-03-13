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
public class Classify implements Serializable {

    //private static final long serialVersionUID = 1L;
    //public static final int TEST_BASE = 0;
    //public static final int TEST_ALL = 1;
    //public static final int TEST_SINGLE = 2;
    //public static final int TEST_PATTERN = 3;
    
    //public static final int RESULT_ERROR = 0;
    //public static final int RESULT_SUCCESS = 1;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int period;
    
    @Column(name = "period_calculation")
    private int periodCalculation;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classifier_id", referencedColumnName = "id")
    private Classifier classifier;

    @ManyToMany()
    private List<Variable> variable;

}
