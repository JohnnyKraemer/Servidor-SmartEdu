package br.com.smartedu.repository;

import br.com.smartedu.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByName(String name);
    
    List<Course> findByUseClassify(int useClassify);

    List<Course> findAllByOrderByIdAsc();

    List<Course> findAllByOrderByIdDesc();

    List<Course> findTop10ByOrderByIdDesc();
}
