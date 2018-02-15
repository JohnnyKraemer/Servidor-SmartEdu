package br.com.smartedu.repository;

import br.com.smartedu.model.Probability;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProbabilityRepository extends JpaRepository<Probability, Long> {

    List<Probability> findAllByOrderByIdAsc();

    List<Probability> findAllByOrderByIdDesc();

    List<Probability> findTop10ByOrderByIdDesc();
}
