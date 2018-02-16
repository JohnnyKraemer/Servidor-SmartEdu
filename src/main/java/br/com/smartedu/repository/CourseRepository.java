package br.com.smartedu.repository;

import br.com.smartedu.model.Course;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByName(String name);
    
    List<Course> findByUseClassify(int useClassify);

    List<Course> findAllByOrderByIdAsc();

    List<Course> findAllByOrderByIdDesc();

    List<Course> findTop10ByOrderByIdDesc();
}
