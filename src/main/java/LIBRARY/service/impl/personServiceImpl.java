package LIBRARY.service.impl;

import LIBRARY.entity.Book;
import LIBRARY.entity.Person;
import LIBRARY.repository.bookRepo;
import LIBRARY.repository.personRepo;
import LIBRARY.service.personService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class personServiceImpl implements personService {

    private final personRepo personRepo;

    @Autowired
    public personServiceImpl(personRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public Person create(Person person) {
      return personRepo.save(person);
    }

    @Transactional(readOnly = true)
    @Override
    public Person findById(Integer id) {
        Optional<Person> foundPerson = personRepo.findById(id);
        return foundPerson.orElse(null);
    }

    @Override
    public Person update(Person person, Integer id) {
        person.setId(id);
        return personRepo.save(person);
    }

    @Override
    public void delete(Integer id) {
        personRepo.delete(findById(id));
    }

    @Override
    public List<Person> findByTitleStartingWith(String startsWith) {
        return personRepo.findByFullNameStartingWith(startsWith);
    }

    @Override
    public Optional<Person> findByFullName(String fullName) {
        return personRepo.findByFullName(fullName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Person> findAll(boolean sort) {
        if(sort)
            return personRepo.findAll(Sort.by("fullName"));
        else
            return personRepo.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Person> findAllByPage(Integer page , Integer peoplePerPage , boolean sort) {
        if(sort)
           return personRepo.findAll(PageRequest.of(page,peoplePerPage,Sort.by("fullName")));
        return personRepo.findAll(PageRequest.of(page,peoplePerPage));
    }


    @Transactional(readOnly = true)
    @Override
    public List<Person> findAll(Integer page , Integer peoplePerPage , boolean sort) {
        if(sort)
            personRepo.findAll(PageRequest.of(page,peoplePerPage,Sort.by("fullName"))).getContent();
       return personRepo.findAll(PageRequest.of(page,peoplePerPage)).getContent();
    }

    public boolean isOverdue(Book book){
        if (book.getTime().plusDays(10).compareTo(LocalDateTime.now())>10){
            return true;
        } else if(book.getTime().plusDays(10).compareTo(LocalDateTime.now())<0)
            return true;
        else return false;
    }

    @Transactional
    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = personRepo.findById(id);
        if(person.isPresent()){
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> book.setOverdue(isOverdue(book)));
            return person.get().getBooks();
        }
        else return Collections.emptyList();

    }

}
