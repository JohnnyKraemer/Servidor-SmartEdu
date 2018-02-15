package br.com.smartedu.repository;

import br.com.smartedu.model.Variable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariableRepository extends JpaRepository<Variable, Long>{
        
	Variable findByName(String name);
        
        List<Variable> findByUseClassifier(int useClassifier);
        
        List<Variable> findAllByOrderByIdAsc();
        
        List<Variable> findAllByOrderByIdDesc();
        
        List<Variable> findTop10ByOrderByIdDesc();
}
