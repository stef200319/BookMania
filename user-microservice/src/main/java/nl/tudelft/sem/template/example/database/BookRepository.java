package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}