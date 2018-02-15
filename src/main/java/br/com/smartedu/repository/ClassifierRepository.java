package br.com.smartedu.repository;

import br.com.smartedu.model.Classifier;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassifierRepository extends JpaRepository<Classifier, Long> {

    Classifier findByName(String name);
    
    List<Classifier> findByUseClassify(int useClassify);

    Classifier findById(Long id);

}
