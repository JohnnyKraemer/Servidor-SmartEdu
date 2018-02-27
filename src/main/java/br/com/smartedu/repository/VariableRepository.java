package br.com.smartedu.repository;

import br.com.smartedu.model.Classifier;
import br.com.smartedu.model.Variable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VariableRepository extends JpaRepository<Variable, Long> {

    Variable findByName(String name);

    List<Variable> findByUseClassify(int useClassify);

    List<Variable> findAllByOrderByIdAsc();

    List<Variable> findAllByOrderByIdDesc();

    List<Variable> findTop10ByOrderByIdDesc();

    @Query(value = "SELECT variable.*\n"
            + "FROM test_classifier\n"
            + "LEFT JOIN classifier \n"
            + "ON test_classifier.classifier_id = classifier.id\n"
            + "LEFT JOIN test_classifier_variable \n"
            + "ON test_classifier.id = test_classifier_variable.test_classifier_id\n"
            + "LEFT JOIN variable\n"
            + "ON variable.id = test_classifier_variable.variable_id\n"
            + "WHERE test_classifier.period_calculation = \n"
            + "    (SELECT IFNULL(MAX(t.period_calculation),0) as period_calculation FROM test_classifier t)\n"
            + "AND test_classifier.course_id = :course_id\n"
            + "AND classifier.id = :classifier_id\n"
            + "GROUP BY variable.name\n"
            + "ORDER BY SUM(test_classifier.success) DESC\n"
            + "LIMIT 5;", nativeQuery = true)
    List<Variable> findTop3VariableByCourseAndClassifier(@Param("course_id") Long course_id, @Param("classifier_id") Long classifier_id);

        @Query(value = "SELECT variable.*\n"
            + "FROM test_classifier\n"
            + "LEFT JOIN classifier \n"
            + "ON test_classifier.classifier_id = classifier.id\n"
            + "LEFT JOIN test_classifier_variable \n"
            + "ON test_classifier.id = test_classifier_variable.test_classifier_id\n"
            + "LEFT JOIN variable\n"
            + "ON variable.id = test_classifier_variable.variable_id\n"
            + "WHERE test_classifier.period_calculation = \n"
            + "    (SELECT IFNULL(MAX(t.period_calculation),0) as period_calculation FROM test_classifier t WHERE t.period = :period )\n"
            + "AND test_classifier.course_id = :course_id\n"
            + "AND classifier.id = :classifier_id\n"
            + "GROUP BY variable.name\n"
            + "ORDER BY SUM(test_classifier.success) DESC\n"
            + "LIMIT 5;", nativeQuery = true)
    List<Variable> findTop3VariableByCourseAndClassifierAndPeriod(@Param("course_id") Long course_id, @Param("classifier_id") Long classifier_id, @Param("period") int period);

    
    
}
