package br.com.smartedu.repository;

import br.com.smartedu.model.Classifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassifierRepository extends JpaRepository<Classifier, Long> {

    Classifier findByName(String name);

    List<Classifier> findByUseClassify(int useClassify);

    Classifier findById(Long id);

    @Query(value = "SELECT classifier.*\n"
            + "FROM test_classifier\n"
            + "LEFT JOIN classifier\n"
            + "ON test_classifier.classifier_id = classifier.id\n"
            + "WHERE test_classifier.period_calculation = (\n"
            + "    SELECT IFNULL(MAX(t.period_calculation),0) as period_calculation \n"
            + "    FROM test_classifier t  WHERE t.type = 0)\n"
            + "AND test_classifier.course_id = :course_id\n"
            + "GROUP BY classifier.name\n"
            + "ORDER BY SUM(test_classifier.success) DESC\n"
            + "LIMIT :limit ;", nativeQuery = true)
    List<Classifier> findTopXClassifiersByCourse(@Param("course_id") Long course_id, @Param("limit") int limit);
    
    @Query(value = "SELECT classifier.*\n"
            + "FROM test_classifier\n"
            + "LEFT JOIN classifier\n"
            + "ON test_classifier.classifier_id = classifier.id\n"
            + "WHERE test_classifier.period_calculation = \n"
            + "    (SELECT IFNULL(MAX(t.period_calculation),0) as period_calculation \n"
            + "    FROM test_classifier t \n"
            + "    WHERE t.period = :period ) \n"
            + "AND test_classifier.course_id = :course_id\n"
            + "GROUP BY classifier.name\n"
            + "ORDER BY SUM(test_classifier.success) DESC\n"
            + "LIMIT 3;", nativeQuery = true)
    List<Classifier> findTop3ClassifiersByCourseByPeriod(@Param("course_id") Long course_id,@Param("period") int period);
}
