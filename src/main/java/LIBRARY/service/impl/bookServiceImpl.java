package LIBRARY.service.impl;

import LIBRARY.entity.Book;
import LIBRARY.entity.Person;
import LIBRARY.repository.bookRepo;
import LIBRARY.service.bookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class bookServiceImpl implements bookService {
    private final bookRepo bookRepo;

    @Autowired
    public bookServiceImpl(bookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll(Integer page, Integer booksPerPage, boolean sort) {
        if(sort)
            return bookRepo.findAll(PageRequest.of(page,booksPerPage,Sort.by("year"))).getContent();
        return bookRepo.findAll(PageRequest.of(page,booksPerPage)).getContent();
    }

    //
    @Transactional(readOnly = true)
    @Override
    public Page<Book> findAllByPage(Integer page, Integer booksPerPage, boolean sort) {
        if(sort)
            return bookRepo.findAll(PageRequest.of(page,booksPerPage,Sort.by("year")));
        return bookRepo.findAll(PageRequest.of(page,booksPerPage));
    }
    //


    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll(boolean sort) {
        if(sort)
            return bookRepo.findAll(Sort.by("year"));
        else return bookRepo.findAll();
    }

    @Override
    public Book create(Book book) {
        return bookRepo.save(book);
    }

    @Override
    public List<Book> findByTitleStartingWith(String startsWith) {
        return bookRepo.findByTitleStartingWith(startsWith);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(Integer id) {
        Optional<Book> foundBook = bookRepo.findById(id);
        return  foundBook.orElse(null);
    }

    @Override
    public void update( Integer id,Book updatedBook) {
        Book book = bookRepo.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setOwner(book.getOwner());
        bookRepo.save(updatedBook);
    }

    @Override
    public void delete(Integer id) {
        bookRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void release(Integer id) {
        bookRepo.findById(id).ifPresent(book -> {
            book.setTime(null);
            book.setOwner(null);
        });

    }

    @Override
    @Transactional
    public void assign (Integer id,Person selectedPerson) {
        bookRepo.findById(id).ifPresent(
                book -> {
                    book.setTime(LocalDateTime.now());
                    book.setOwner(selectedPerson);
                });
    }

    @Override
    @Transactional
    public Person getBookOwner(Integer id) {
        return bookRepo.findById(id).map(Book::getOwner).orElse(null);
    }

}
