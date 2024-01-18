package nl.tudelft.sem.template.example.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.model.Book;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final transient BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Updates the information of a book with the provided details.
     *
     * <p>
     * This method fetches the book with the specified ID from the database, updates its
     * details with the information from the provided updated book, and saves the changes
     * back to the database.
     * </p>
     *
     * @param updatedBook The book object containing the updated information.
     * @param id The unique identifier of the book to be updated.
     */
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

    /**
     * Increments the read count of a book with the specified ID.
     *
     * <p>
     * This method retrieves the book with the given ID from the repository, increments
     * its read count by one, and then saves the updated book back to the repository.
     * </p>
     *
     * @param id The unique identifier of the book to mark as read.
     */
    public void readBook(long id) {
        Book bookToRead = bookRepository.getOne(id);
        bookToRead.setReads(bookToRead.getReads() + 1);
        this.bookRepository.saveAndFlush(bookToRead);
    }

    /**
     * Retrieves a list of books based on the provided list of book IDs.
     *
     * <p>
     * This method iterates through the given list of book IDs, attempts to convert each ID to
     * a Long value, checks if the book with that ID exists in the repository, and adds the
     * corresponding book to the result list. If an invalid ID is encountered, it is ignored,
     * and a message is printed to the console. The resulting list of books is then returned.
     * </p>
     *
     * @param ids A list of strings representing book IDs.
     * @return A list of books corresponding to the provided book IDs.
     */
    public List<Book> getBooks(List<String> ids) {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            try {
                Long id = Long.parseLong(ids.get(i));
                if (bookRepository.existsById(id)) {
                    books.add(bookRepository.getOne(id));
                }
            } catch (NumberFormatException e) {
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

    /**
     * Retrieves a list of books based on specified search criteria and sorting options.
     *
     * <p>
     * This method constructs an example book object using the provided parameters
     * (author, genre, title, description, series) and performs a case-insensitive,
     * partial matching search in the repository using Spring Data JPA's Example API.
     * The result is then sorted based on the specified sorting criteria.
     * </p>
     *
     * @param author The author's name to search for in books.
     * @param genre The genre to search for in books.
     * @param title The title to search for in books.
     * @param description The description to search for in books.
     * @param series The series to search for in books.
     * @param sortBy The sorting criteria for the result (read_count, alphabetical,
     *               read_count_reversed, alphabetical_reversed).
     * @return A list of books matching the specified criteria and sorted according to the
     *         provided sorting criteria.
     */
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
            default -> {
                books.sort(Comparator.comparing(Book::getTitle));
            }

        }
        return books;

    }
}
