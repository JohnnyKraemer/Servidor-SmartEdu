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
public class TestClassifier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "period_calculation")
    private String periodCalculation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classifier_id", referencedColumnName = "id")
    private Classifier classifier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    private int success;

    private int failure;

    private int seccess_evaded;

    private int seccess_not_evaded;

    private int failure_evaded;

    private int failure_not_evaded;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date start;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date end;

    private int time_seconds;
    
    private int type;
    
    private int result;

    @ManyToMany()
    private List<Variable> variable;

}
