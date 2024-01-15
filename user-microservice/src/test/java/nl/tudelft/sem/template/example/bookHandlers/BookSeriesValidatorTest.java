package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAuthorException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookIdException;
import nl.tudelft.sem.template.example.model.Book;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookSeriesValidatorTest {
    @Test
    public void testNullSeries() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setSeries(null);
        BookSeriesValidator bookSeriesValidator = new BookSeriesValidator(bookRepository);
        assertThrows(InvalidBookException.class,()->{bookSeriesValidator.handle(book);});
    }
    @Test
    public void testEmptySeries() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setSeries("");
        BookSeriesValidator bookSeriesValidator = new BookSeriesValidator(bookRepository);
        assertThrows(InvalidBookException.class,()->{bookSeriesValidator.handle(book);});
    }
    @Test
    public void testValidSeries() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setSeries("series");
        BookSeriesValidator bookSeriesValidator = new BookSeriesValidator(bookRepository);
        assertTrue(bookSeriesValidator.handle(book));
    }


}

