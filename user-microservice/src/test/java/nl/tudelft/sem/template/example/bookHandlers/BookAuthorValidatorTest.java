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

public class BookAuthorValidatorTest {
    @Test
    public void testNullAuthor() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setAuthor(null);
        BookAuthorValidator bookAuthorValidator = new BookAuthorValidator(bookRepository);
        assertThrows(InvalidAuthorException.class,()->{bookAuthorValidator.handle(book);});
    }
    @Test
    public void testEmptyAuthor() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setAuthor("");
        BookAuthorValidator bookAuthorValidator = new BookAuthorValidator(bookRepository);
        assertThrows(InvalidAuthorException.class,()->{bookAuthorValidator.handle(book);});
    }
    @Test
    public void testInvalidAuthor() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setAuthor("MihaiEminescu777");
        BookAuthorValidator bookAuthorValidator = new BookAuthorValidator(bookRepository);
        assertThrows(InvalidAuthorException.class,()->{bookAuthorValidator.handle(book);});
    }
    @Test
    public void testValidDescription() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setAuthor("Mihai Eminescu");
        BookAuthorValidator bookAuthorValidator = new BookAuthorValidator(bookRepository);
        assertTrue(bookAuthorValidator.handle(book));
    }


}
