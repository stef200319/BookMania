package nl.tudelft.sem.template.example.services;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Book;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void updateBook(Book updatedBook, long id) {
        // Fetch book from database
        Book opBook = this.bookRepository.getOne(id);

        // Update the book
        opBook.setAuthor(updatedBook.getAuthor());
        opBook.setTitle(updatedBook.getTitle());
        opBook.setDescription(updatedBook.getDescription());
        opBook.setReads(updatedBook.getReads());
        opBook.setSeries(updatedBook.getSeries());
        opBook.setGenres(updatedBook.getGenres());

        // Save the changes
        this.bookRepository.saveAndFlush(opBook);
    }

    public void readBook(long id) {
        Book bookToRead = bookRepository.getOne(id);
        bookToRead.setReads(bookToRead.getReads() + 1);
        this.bookRepository.saveAndFlush(bookToRead);
    }

    public List<Book> getBooks(List<String> ids) {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            try {
                Long id = Long.parseLong(ids.get(i));
                if (bookRepository.existsById(id)) {
                    books.add(bookRepository.getOne(id));
                }
            }
            catch (NumberFormatException e) {
                System.out.println("invalid id");
            }
        }

        return books;
    }

    public Book createBook(Book book) {
        return bookRepository.saveAndFlush(book);
    }

    public Book getBook(long id) {
        return bookRepository.getOne(id);
    }

    public void deleteBook(long id) {
        this.bookRepository.deleteById(id);
    }

    public List<Book> findBook(String author, String genre, String title, String description, String series, String sortBy) {
        Book exampleBook = new Book();
        exampleBook.setAuthor(author);
        exampleBook.setGenres(Collections.singletonList(genre));
        exampleBook.setTitle(title);
        exampleBook.setDescription(description);
        exampleBook.setSeries(series);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnoreNullValues();

        Example<Book> example = Example.of(exampleBook, matcher);
        List<Book> books = bookRepository.findAll(example);
        switch (sortBy) {
            case "read_count" -> {
                books.sort(Comparator.comparing(Book::getReads).reversed());
            }
            case "alphabetical" -> {
                books.sort(Comparator.comparing(Book::getTitle));
            }
            case "read_count_reversed" -> {
                books.sort(Comparator.comparing(Book::getReads));
            }
            case "alphabetical_reversed" -> {
                books.sort(Comparator.comparing(Book::getTitle).reversed());
            }
        }
        return books;

    }
}
