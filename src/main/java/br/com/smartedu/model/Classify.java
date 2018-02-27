package br.com.smartedu.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
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
