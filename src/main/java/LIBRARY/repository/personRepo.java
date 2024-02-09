package LIBRARY.repository;

import LIBRARY.entity.Book;
import LIBRARY.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface personRepo extends JpaRepository<Person,Integer> {
    Optional<Person> findByFullName(String fullName);
    List<Person> findByFullNameStartingWith(String startsWith);
}
