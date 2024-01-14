package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.model.Book;

public class BookDescriptionValidator extends BaseBookValidator{
    public BookDescriptionValidator(BookRepository bookRepository){
        super(bookRepository);
    }
    @Override
    public boolean handle(Book book) throws InvalidBookException {
        if(book.getDescription().isBlank()){
            throw new InvalidBookException("Book must have a description");
        }
        return this.checkNext(book);
    }
}
