package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.finaltest.models.Person;

import java.util.Collection;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, String> {
    List<Person> findAllByFirstName(String firstName);
}
