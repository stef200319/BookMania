package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.model.Book;

public class BookSeriesValidator extends BaseBookValidator{
    public BookSeriesValidator(BookRepository bookRepository){
        super(bookRepository);
    }
    @Override
    public boolean handle(Book book) throws InvalidBookException {
        if(book.getSeries().isBlank()){
            throw new InvalidBookException("Book must have a series associated to it");
        }
        return this.checkNext(book);
    }
}
