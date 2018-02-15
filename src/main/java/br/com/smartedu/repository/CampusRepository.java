package br.com.smartedu.repository;

import br.com.smartedu.model.Campus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<Campus, Long> {

    Campus findByName(String name);

    List<Campus> findAllByOrderByIdAsc();

    List<Campus> findAllByOrderByIdDesc();

    List<Campus> findTop10ByOrderByIdDesc();
}
