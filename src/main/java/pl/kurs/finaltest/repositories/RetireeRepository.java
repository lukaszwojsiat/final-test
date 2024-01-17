package pl.kurs.finaltest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.finaltest.models.Retiree;

import java.util.List;

public interface RetireeRepository extends JpaRepository<Retiree, Long> {
    List<Retiree> findAllByFirstName(String firstName);

    List<Retiree> findAllByLastName(String lastName);
}
