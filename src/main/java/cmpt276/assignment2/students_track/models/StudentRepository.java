package cmpt276.assignment2.students_track.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer>{
    List<Student> findByName(String name);
    List<Student> findAllByOrderBySid();
    Student findBySid(int sid);
    void deleteBySid(int sid);

}
