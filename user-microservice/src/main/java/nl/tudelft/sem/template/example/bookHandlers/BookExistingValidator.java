package nl.tudelft.sem.template.example.bookHandlers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidBookException;
import nl.tudelft.sem.template.example.model.Book;

public class BookExistingValidator extends BaseBookValidator{
    public BookExistingValidator(BookRepository bookRepository){
        super(bookRepository);
    }
    @Override
    public boolean handle(Book book) throws InvalidBookException {

        long bookId = book.getId();
        if(!bookRepository.existsById(bookId)){
            throw new InvalidBookException("Book does not exist");
        }
        Book bookInRepo = bookRepository.findById(bookId).get();
        return this.checkNext(bookInRepo);
    }
}
