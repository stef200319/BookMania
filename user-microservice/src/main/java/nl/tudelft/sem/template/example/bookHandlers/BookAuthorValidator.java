package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.exceptions.InvalidDataException;
import nl.tudelft.sem.template.example.model.Book;

public class BookAuthorValidator extends BaseBookValidator{
    public BookAuthorValidator(BookRepository bookRepository){
        super(bookRepository);
    }
    @Override
    public boolean handle(Book book) throws InvalidBookException {
        if(book.getAuthor().isBlank()){
            throw new InvalidBookException("Book must have an author");
        }
        if(!book.getTitle().trim().replaceAll("\\s","").matches("^[a-zA-Z]*")) {
            throw new InvalidBookException("The author name contains illegal characters.");
        }
        return this.checkNext(book);
    }
}
