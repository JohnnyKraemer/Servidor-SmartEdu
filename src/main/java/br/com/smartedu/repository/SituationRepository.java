package br.com.smartedu.repository;

import br.com.smartedu.model.Situation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SituationRepository extends JpaRepository<Situation, Long> {

    Situation findBySituationLong(String situationLong);

    Situation findBySituationShort(String situationShort);

    List<Situation> findAllByOrderByIdAsc();

    List<Situation> findAllByOrderByIdDesc();

    List<Situation> findTop10ByOrderByIdDesc();
}
