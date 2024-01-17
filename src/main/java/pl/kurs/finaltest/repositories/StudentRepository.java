package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.finaltest.models.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByFirstName(String firstName);

    List<Student> findAllByLastName(String lastName);
}

