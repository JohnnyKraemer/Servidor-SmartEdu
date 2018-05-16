package br.com.smartedu.repository;

import br.com.smartedu.model.Classifier;
import br.com.smartedu.model.Course;
import br.com.smartedu.model.TestClassifier;
import br.com.smartedu.model.Variable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestClassifierRepository extends JpaRepository<TestClassifier, Long> {

    @Query(value = "SELECT IFNULL(MAX(t.period_calculation),1) as period_calculation FROM test_classifier t", nativeQuery = true)
    int findMaxPeriodCalculation();
    
    @Query(value = "SELECT IFNULL(MAX(test_classifier.period_calculation),1) as period_calculation\n"
            + "FROM test_classifier\n"
            + "WHERE test_classifier.course_id = :course_id;", nativeQuery = true)
    int findMaxPeriodCalculationByCourse(@Param("course_id") Long course_id);
    
    @Query(value = "SELECT IFNULL(MAX(test_classifier.period_calculation),1) as period_calculation\n"
            + "FROM test_classifier\n"
            + "WHERE test_classifier.type = :type ;", nativeQuery = true)
    int findMaxPeriodCalculationByType(@Param("type") int type);
    
    @Query(value = "SELECT IFNULL(MAX(test_classifier.period_calculation),1) as period_calculation\n"
            + "FROM test_classifier\n"
            + "WHERE test_classifier.type = :type\n"
            + "AND test_classifier.course_id = :course_id ;", nativeQuery = true)
    int findMaxPeriodCalculationByTypeAndCourse(@Param("type") int type, @Param("course_id") Long course_id);
    
    @Query(value = "SELECT IFNULL(MAX(test_classifier.period_calculation),1)\n"
            + "FROM test_classifier\n"
            + "WHERE test_classifier.course_id = :course_id\n"
            + "AND test_classifier.period = :period ;", nativeQuery = true)
    int findMaxPeriodCalculationByCourseAndPeriod(@Param("course_id") Long course_id, @Param("period") int period);
    
    TestClassifier findByPeriodCalculation(int periodCalculation);

    //boolean existsByPeriodCalculation(int periodCalculation);
    
    @Query(value = "SELECT t.* FROM test_classifier t WHERE ", nativeQuery = true)
    List<TestClassifier> findPattern();
    
    List<TestClassifier> findAllByOrderByIdAsc();

    List<TestClassifier> findAllByOrderByIdDesc();
    
    //TestClassifier findTop1OrderByPeriodCalculationDesc();
    
    TestClassifier findTop1ByCourseAndPeriodCalculationOrderBySuccessDesc(Course course, int periodCalculation);

    TestClassifier findByCourseAndClassifierAndVariableAndPeriodCalculationAndType(Course course, Classifier classifier, List<Variable> variable, int periodCalculation, int type);

    List<TestClassifier> findTop3ByCourseOrderBySuccessDesc(Course course);

    List<TestClassifier> findTop10ByCourseOrderBySuccessDesc(Course course);

    List<TestClassifier> findTop10ByOrderByIdDesc();
    List<TestClassifier> findByIdBetween(Long endId, Long startId);
    List<TestClassifier> findByIdGreaterThan(Long id);

}
