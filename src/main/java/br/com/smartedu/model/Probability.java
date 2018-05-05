package br.com.smartedu.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode
@ToString
public class Probability implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double probability_evasion;

    @Column(nullable = false)
    private String situation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_classifier_id", referencedColumnName = "id")
    private TestClassifier testClassifier;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "situation_id", referencedColumnName = "id")
    //private Situation situation;
    
}
