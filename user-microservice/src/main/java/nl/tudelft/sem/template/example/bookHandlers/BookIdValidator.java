package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.model.Book;

public class BookIdValidator extends BaseBookValidator{
    public BookIdValidator(BookRepository bookRepository){
        super(bookRepository);
    }
    @Override
    public boolean handle(Book book) throws InvalidBookException {
        if(book.getId() == null){
            throw new InvalidBookException("Book must have an id");
        }
        return this.checkNext(book);
    }
}
