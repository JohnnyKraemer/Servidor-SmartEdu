package br.com.smartedu.repository;

import br.com.smartedu.model.Probability;
import br.com.smartedu.model.TestClassifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProbabilityRepository extends JpaRepository<Probability, Long> {

    List<Probability> findAllByOrderByIdAsc();

    List<Probability> findAllByTestClassifier(TestClassifier testClassifier);

    List<Probability> findAllByOrderByIdDesc();

    List<Probability> findTop10ByOrderByIdDesc();
}
