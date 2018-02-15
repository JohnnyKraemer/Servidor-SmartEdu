package br.com.smartedu.repository;

import br.com.smartedu.model.Situation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SituationRepository extends JpaRepository<Situation, Long> {

    //@Query(value = "select count(s.situacao_completa)>0 from situacao_aluno s where s.situacao_completa = ?1", nativeQuery = true)
    //boolean existsBySituacaoCompleta(String situacaoCompleta);

    //Situation findBySituacaoResumida(String situacaoResumida);

    //Situation findBySituacaoCompleta(String situacaoCompleta);

    List<Situation> findAllByOrderByIdAsc();

    List<Situation> findAllByOrderByIdDesc();

    List<Situation> findTop10ByOrderByIdDesc();
}
