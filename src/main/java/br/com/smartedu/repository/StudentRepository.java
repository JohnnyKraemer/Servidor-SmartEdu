package br.com.smartedu.repository;

import br.com.smartedu.model.Course;
import br.com.smartedu.model.Detail;
import br.com.smartedu.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByNome(String nome);

    List<Student> findByCodigo(Long codigo);

    List<Student> findByCourse(Course course);

    @Query(value = "SELECT student.* \n"
            + "FROM student \n"
            + "LEFT JOIN student_detail\n"
            + "ON student.id = student_detail.student_id\n"
            + "LEFT JOIN detail\n"
            + "ON detail.id = student_detail.detail_id\n"
            + "LEFT JOIN situation\n"
            + "ON situation.id = detail.situation_id\n"
            + "WHERE detail.id = \n"
                + "	(SELECT  max(detail.id)\n"
                + "	FROM student s\n"
                + "	LEFT JOIN student_detail\n"
                + "	ON s.id = student_detail.student_id\n"
                + "	LEFT JOIN detail\n"
                + "	ON detail.id = student_detail.detail_id\n"
                + "	WHERE s.id = student.id)\n"
            + "AND student.course_id = :course_id\n"
            + "AND (situation.situation_short LIKE 'Evadido' OR situation.situation_short LIKE 'Formado')\n"
            + "ORDER BY student.id;", nativeQuery = true)
    List<Student> findByCourse(@Param("course_id") Long course_id);
    
    @Query(value = "SELECT student.* \n"
            + "FROM student \n"
            + "LEFT JOIN student_detail\n"
            + "ON student.id = student_detail.student_id\n"
            + "LEFT JOIN detail\n"
            + "ON detail.id = student_detail.detail_id\n"
            + "LEFT JOIN situation\n"
            + "ON situation.id = detail.situation_id\n"
            + "WHERE detail.id = \n"
                + "	(SELECT  max(detail.id)\n"
                + "	FROM student s\n"
                + "	LEFT JOIN student_detail\n"
                + "	ON s.id = student_detail.student_id\n"
                + "	LEFT JOIN detail\n"
                + "	ON detail.id = student_detail.detail_id\n"
                + "	WHERE s.id = student.id)\n"
            + "AND student.course_id = :course_id\n"
            + "AND (situation.situation_short LIKE 'Não Evadido')\n"
            + "ORDER BY student.id;", nativeQuery = true)
    List<Student> findByCourseTest(@Param("course_id") Long course_id);
    
        @Query(value = "SELECT student.* \n"
            + "FROM student \n"
            + "LEFT JOIN student_detail\n"
            + "ON student.id = student_detail.student_id\n"
            + "LEFT JOIN detail\n"
            + "ON detail.id = student_detail.detail_id\n"
            + "LEFT JOIN situation\n"
            + "ON situation.id = detail.situation_id\n"
            + "WHERE detail.id = \n"
                + "	(SELECT  max(detail.id)\n"
                + "	FROM student s\n"
                + "	LEFT JOIN student_detail\n"
                + "	ON s.id = student_detail.student_id\n"
                + "	LEFT JOIN detail\n"
                + "	ON detail.id = student_detail.detail_id\n"
                + "	WHERE  detail.periodo_carga != (SELECT MAX(detail.periodo_carga) FROM detail)\n"
                + "     AND detail.periodo = :period\n"
                + "     AND s.id = student.id)\n"
            + "AND student.course_id = :course_id\n"
            + "AND (situation.situation_short LIKE 'Evadido' OR situation.situation_short LIKE 'Não Evadido')\n"
            + "ORDER BY student.id;", nativeQuery = true)
    List<Student> findByCourseAndPeriod(@Param("course_id") Long course_id, @Param("period") int period);

    List<Student> findByDetail(Detail detail);

    List<Student> findAllByOrderByIdAsc();

    List<Student> findAllByOrderByIdDesc();

    List<Student> findTop10ByOrderByIdDesc();

}
