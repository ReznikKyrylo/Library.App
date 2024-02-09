package LIBRARY.service;

import LIBRARY.entity.Book;
import LIBRARY.entity.Person;
import org.springframework.data.domain.Page;

import java.util.List;


public interface bookService {
    Person getBookOwner(Integer id);
    Book create(Book book);
    Book findById(Integer id);
    void update(Integer id,Book book);
    void delete(Integer id);
    void release(Integer id);
    void assign (Integer id,Person selectedPerson);

    List<Book> findByTitleStartingWith(String startsWith);
    List<Book> findAll(Integer page , Integer booksPerPage , boolean sort);
    List<Book> findAll(boolean sort);

    Page<Book> findAllByPage(Integer page, Integer booksPerPage, boolean sort);

}
