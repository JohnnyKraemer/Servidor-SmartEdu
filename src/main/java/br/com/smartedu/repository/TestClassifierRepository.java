package br.com.smartedu.repository;

import br.com.smartedu.model.Course;
import br.com.smartedu.model.TestClassifier;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestClassifierRepository extends JpaRepository<TestClassifier, Long> {

    //@Query(value = "SELECT t FROM TestClassifier t WHERE t.period_calculation = :period_calculation")
    //TestClassifier teste(@Param("period_calculation") String period_calculation);
    TestClassifier findByPeriodCalculation(String periodCalculation);

    boolean existsByPeriodCalculation(String periodCalculation);

    List<TestClassifier> findAllByOrderByIdAsc();

    List<TestClassifier> findAllByOrderByIdDesc();
    
    TestClassifier findTop1OrderByPeriodCalculationDesc();

    List<TestClassifier> findTop3ByCourseOrderBySuccessDesc(Course course);

    List<TestClassifier> findTop10ByCourseOrderBySuccessDesc(Course course);

    List<TestClassifier> findTop10ByOrderByIdDesc();

}
