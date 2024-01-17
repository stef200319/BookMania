package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAuthorException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookIdException;
import nl.tudelft.sem.template.example.model.Book;

public abstract class BaseBookValidator implements BookValidator {
    private BookValidator next;
    protected BookRepository bookRepository;

    public BaseBookValidator(BookRepository bookRepository) {
        this.bookRepository =   bookRepository;
    }

    public void setNext(BookValidator handler) {
        this.next = handler;
    }

    protected boolean checkNext(Book book) throws InvalidBookException, InvalidAuthorException, InvalidBookIdException {
        if (this.next == null) {
            return true;
        }
        return next.handle(book);
    }
}
