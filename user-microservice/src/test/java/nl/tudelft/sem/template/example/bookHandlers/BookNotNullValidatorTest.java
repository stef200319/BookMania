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

public class BookNotNullValidatorTest {
    @Test
    public void testBookNotNull() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException {
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setId(1L);
        BookNotNullValidator bookNotNullValidator = new BookNotNullValidator(bookRepository);
        assertTrue(bookNotNullValidator.handle(book));
    }

    @Test
    public void testBookNull() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        BookNotNullValidator bookNotNullValidator = new BookNotNullValidator(bookRepository);
        Book book = null;
        assertThrows(InvalidBookException.class,()->{bookNotNullValidator.handle(book);});
    }
}
