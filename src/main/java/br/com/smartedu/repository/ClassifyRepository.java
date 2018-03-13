package br.com.smartedu.repository;

import br.com.smartedu.model.Classify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassifyRepository extends JpaRepository<Classify, Long> {

    @Query(value = "SELECT IFNULL(MAX(classify.period_calculation),0) as period_calculation FROM classify", nativeQuery = true)
    int findMaxPeriodCalculation();

    @Query(value = "SELECT classify.*\n"
            + "FROM course\n"
            + "LEFT JOIN course_classify\n"
            + "ON course_classify.course_id = course.id\n"
            + "LEFT JOIN classify\n"
            + "ON classify.id = course_classify.classify_id\n"
            + "WHERE course.id = :course_id\n"
            + "AND classify.period_calculation = \n"
            + "	(SELECT IFNULL(MAX(classify.period_calculation),0) as period_calculation FROM classify)", nativeQuery = true)
    List<Classify> findByCourseAndMaxPeriodCalcularion(@Param("course_id") Long course_id);

    Classify findByPeriodCalculation(int periodCalculation);

    List<Classify> findAllByOrderByIdAsc();

    List<Classify> findAllByOrderByIdDesc();
}
