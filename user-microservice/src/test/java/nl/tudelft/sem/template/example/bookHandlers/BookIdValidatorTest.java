package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAuthorException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookIdException;
import nl.tudelft.sem.template.example.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookIdValidatorTest {
    @Test
    public void nullIdTest() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setId(null);
        BookIdValidator bookIdValidator = new BookIdValidator(bookRepository);
        assertThrows(InvalidBookIdException.class,()->{bookIdValidator.handle(book);});
    }
    @Test
    public void validIdTest() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setId(1L);
        BookIdValidator bookIdValidator = new BookIdValidator(bookRepository);
        Assertions.assertTrue(bookIdValidator.handle(book));
    }

}
