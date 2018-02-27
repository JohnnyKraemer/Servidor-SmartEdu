package br.com.smartedu.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import javax.persistence.Entity;

@Entity
@Data
public class Probability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double probability_evasion;

    @Column(nullable = false)
    private String state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_classifier_id", referencedColumnName = "id")
    private TestClassifier testClassifier;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;
    
}
