package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAuthorException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookIdException;
import nl.tudelft.sem.template.example.model.Book;

public class BookTitleValidator extends BaseBookValidator {
    public BookTitleValidator(BookRepository bookRepository) {
        super(bookRepository);
    }

    @Override
    public boolean handle(Book book) throws InvalidBookException, InvalidAuthorException, InvalidBookIdException {
        if (book.getTitle() == null) {
            throw new InvalidBookException("Book must have a title");
        }
        if (book.getTitle().isBlank()) {
            throw new InvalidBookException("Book must have a title");
        }
        return this.checkNext(book);
    }
}
