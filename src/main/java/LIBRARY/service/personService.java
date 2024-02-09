package LIBRARY.service;

import LIBRARY.entity.Book;
import LIBRARY.entity.Person;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface personService {
    Person create(Person person);
    Person findById(Integer id);
    Person update(Person person , Integer id);
    void delete(Integer id);

    List<Person> findByTitleStartingWith(String startsWith);
    List<Book> getBooksByPersonId(int id);
    List<Person> findAll(Integer page , Integer peoplePerPage , boolean sort);
    List<Person> findAll(boolean sort);
    Optional<Person> findByFullName(String fullName);

    Page<Person> findAllByPage(Integer page , Integer peoplePerPage , boolean sort);


}
