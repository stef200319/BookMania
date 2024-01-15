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

public class BookTitleValidatorTest {
    @Test
    public void testNullTitle() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setTitle(null);
        BookTitleValidator bookTitleValidator = new BookTitleValidator(bookRepository);
        assertThrows(InvalidBookException.class,()->{bookTitleValidator.handle(book);});
    }
    @Test
    public void testEmptySeries() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setTitle("");
        BookTitleValidator bookTitleValidator = new BookTitleValidator(bookRepository);
        assertThrows(InvalidBookException.class,()->{bookTitleValidator.handle(book);});
    }
    @Test
    public void testValidSeries() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setTitle("title");
        BookTitleValidator bookTitleValidator = new BookTitleValidator(bookRepository);
        assertTrue(bookTitleValidator.handle(book));
    }


}
