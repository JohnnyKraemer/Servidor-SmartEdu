package br.com.smartedu.repository;

import br.com.smartedu.model.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, Long> {

    
    @Query(value = "select max(d.loading_period) from Detail d", nativeQuery = true)
    String findMaxPeriodo_Carga();
   
    Detail findOneByLoadingPeriod(String loadingPeriod);
    
    List<Detail> findAllByOrderByIdAsc();

    List<Detail> findAllByOrderByIdDesc();

    List<Detail> findTop10ByOrderByIdDesc();
}
