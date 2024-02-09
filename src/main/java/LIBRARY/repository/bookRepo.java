package LIBRARY.repository;

import LIBRARY.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface bookRepo extends JpaRepository<Book,Integer> {
    List<Book> findByTitleStartingWith(String startsWith);


}
