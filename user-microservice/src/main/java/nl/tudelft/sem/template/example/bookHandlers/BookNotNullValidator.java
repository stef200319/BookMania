package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAuthorException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookIdException;
import nl.tudelft.sem.template.example.model.Book;

public class BookNotNullValidator extends BaseBookValidator {
    public BookNotNullValidator(BookRepository bookRepository) {
        super(bookRepository);
    }

    @Override
    public boolean handle(Book book) throws InvalidBookException, InvalidAuthorException, InvalidBookIdException {
        if (book == null) {
            throw new InvalidBookException("Book cannot be null");
        }

        return this.checkNext(book);
    }
}
