package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAuthorException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookIdException;
import nl.tudelft.sem.template.example.model.Book;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookGenresValidatorTest {
    @Test
    public void testNullGenres() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setGenres(null);
        BookGenresValidator bookGenresValidator = new BookGenresValidator(bookRepository);
        assertThrows(InvalidBookException.class,()->{bookGenresValidator.handle(book);});
    }
    @Test
    public void testEmptySeries() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setGenres(new ArrayList<String>());
        BookGenresValidator bookGenresValidator = new BookGenresValidator(bookRepository);
        assertThrows(InvalidBookException.class,()->{bookGenresValidator.handle(book);});
    }
    @Test
    public void testValidSeries() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        ArrayList<String> genres = new ArrayList<>();
        genres.add("romance");
        book.setGenres(genres);
        BookGenresValidator bookGenresValidator = new BookGenresValidator(bookRepository);
        assertTrue(bookGenresValidator.handle(book));
    }


}
