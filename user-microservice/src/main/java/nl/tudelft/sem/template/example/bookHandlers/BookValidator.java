package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.model.Book;

public interface BookValidator {
    void setNext(BookValidator handler);
    boolean handle(Book book) throws InvalidBookException;
}
