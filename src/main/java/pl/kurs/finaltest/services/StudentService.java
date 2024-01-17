package pl.kurs.finaltest.services;

import org.springframework.stereotype.Service;
import pl.kurs.finaltest.models.Person;
import pl.kurs.finaltest.repositories.PersonRepository;
import pl.kurs.finaltest.repositories.StudentRepository;

@Service
public class StudentService {
    private StudentRepository repository;
    private PersonRepository personRepository;

//    public List<Student> getStudentsBy

    public void addPerson(Person person) {
        personRepository.save(person);
    }
}
