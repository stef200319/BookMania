package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.model.Book;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookExistingValidatorTest {
    @Test
    public void testHandleExistingBook() throws InvalidBookException {
        BookRepository bookRepository = Mockito.mock(BookRepository.class);

        Book existingBook = new Book();
        existingBook.setId(1L);

        Mockito.when(bookRepository.existsById(existingBook.getId())).thenReturn(true);
        Mockito.when(bookRepository.findById(existingBook.getId())).thenReturn(Optional.of(existingBook));
        BookExistingValidator bookExistingValidator = new BookExistingValidator(bookRepository);
        boolean result = bookExistingValidator.handle(existingBook);
        assertTrue(result);
    }
    @Test
    public void testHandleNotExistingBook() throws InvalidBookException {
        BookRepository bookRepository = Mockito.mock(BookRepository.class);
        Book book = new Book();
        book.setId(1L);

        Mockito.when(bookRepository.existsById(book.getId())).thenReturn(false);
        BookExistingValidator bookExistingValidator = new BookExistingValidator(bookRepository);
        assertThrows(InvalidBookException.class, ()->{bookExistingValidator.handle(book);});
    }
}
