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
public class TestClassifier implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int TEST_BASE = 0;
    public static final int TEST_ALL = 1;
    public static final int TEST_SINGLE = 2;
    public static final int TEST_PATTERN = 3;

    //public static final int TEST_PERIOD_BASE = 4;
    //public static final int TEST_PERIOD_ALL = 5;
    //public static final int TEST_PERIOD_SINGLE = 6;
    //public static final int TEST_PERIOD_PATTERN = 7;

    public static final int PATTERN_TEST = 8;
    public static final int PATTERN_RESULT = 9;

    public static final int RESULT_ERROR = 0;
    public static final int RESULT_SUCCESS = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "period_calculation")
    private int periodCalculation;

    private int period;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classifier_id", referencedColumnName = "id")
    private Classifier classifier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    private int success;

    private int failure;

    private int success_evaded;

    private int success_not_evaded;

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
