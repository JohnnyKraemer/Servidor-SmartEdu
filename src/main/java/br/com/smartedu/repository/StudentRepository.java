package br.com.smartedu.repository;

import br.com.smartedu.model.Course;
import br.com.smartedu.model.Detail;
import br.com.smartedu.model.Student;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByNome(String nome);

    List<Student> findByCodigo(Long codigo);

    List<Student> findByCourse(Course course);

    List<Student> findByDetail(Detail detail);

    List<Student> findAllByOrderByIdAsc();

    List<Student> findAllByOrderByIdDesc();

    List<Student> findTop10ByOrderByIdDesc();


}
