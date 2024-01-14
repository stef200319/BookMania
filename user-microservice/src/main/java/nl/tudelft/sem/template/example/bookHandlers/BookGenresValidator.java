package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.model.Book;

public class BookGenresValidator extends BaseBookValidator{
    public BookGenresValidator(BookRepository bookRepository){
        super(bookRepository);
    }
    @Override
    public boolean handle(Book book) throws InvalidBookException {
        if(book.getGenres().isEmpty()){
            throw new InvalidBookException("Book must have at least one genre");
        }
        return this.checkNext(book);
    }
}
