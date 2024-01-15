package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAuthorException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.exceptions.InvalidBookIdException;
import nl.tudelft.sem.template.example.model.Book;

public class BookIdValidator extends BaseBookValidator{
    public BookIdValidator(BookRepository bookRepository){
        super(bookRepository);
    }
    @Override
    public boolean handle(Book book) throws InvalidBookException, InvalidAuthorException, InvalidBookIdException {
        if(book.getId() == null){
            throw new InvalidBookIdException("Book must have an id");
        }
        return this.checkNext(book);
    }
}
