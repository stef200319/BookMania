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

public class BookDescriptionValidatorTest {
    @Test
    public void testNullDescription() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setDescription(null);
        BookDescriptionValidator bookDescriptionValidator = new BookDescriptionValidator(bookRepository);
        assertThrows(InvalidBookException.class,()->{bookDescriptionValidator.handle(book);});
    }
    @Test
    public void testEmptyDescription() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setDescription("");
        BookDescriptionValidator bookDescriptionValidator = new BookDescriptionValidator(bookRepository);
        assertThrows(InvalidBookException.class,()->{bookDescriptionValidator.handle(book);});
    }
    @Test
    public void testValidDescription() throws InvalidBookException, InvalidAuthorException, InvalidBookIdException{
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setDescription("description");
        BookDescriptionValidator bookDescriptionValidator = new BookDescriptionValidator(bookRepository);
        assertTrue(bookDescriptionValidator.handle(book));
    }


}
