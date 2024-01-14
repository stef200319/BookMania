package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.bookHandlers.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import javax.validation.Valid;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.exceptions.*;
import nl.tudelft.sem.template.example.model.Book;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.services.BookService;
import nl.tudelft.sem.template.example.services.UserService;
import nl.tudelft.sem.template.example.userHandlers.AdminValidator;
import nl.tudelft.sem.template.example.userHandlers.UserExistingValidator;
import nl.tudelft.sem.template.example.userHandlers.UserLoggedInValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    BookRepository bookRepo;
    BookService bookService;

    @Autowired
    public BookController(BookRepository bookRepo, BookService bookService) {
        this.bookRepo = bookRepo;
        this.bookService = bookService;
    }

    @PostMapping("/{username}")
    public ResponseEntity createBook(@PathVariable String username, @RequestBody Book newBook){
        UserExistingValidator userHandler = new UserExistingValidator(userRepo);
        AdminValidator av = new AdminValidator(userRepo);
        UserLoggedInValidator ulv = new UserLoggedInValidator(userRepo);
        av.setNext(ulv);
        userHandler.setNext(av);
        User user = new User();
        user.setUsername(username);
        try {
            userHandler.handle(user);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e){
            if(e.getMessage().equals("User does not exist")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            if(e.getMessage().equals("User is not an admin")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an admin");
            }
        }
        BookIdValidator bookHandler = new BookIdValidator(bookRepo);
        BookAuthorValidator v2 = new BookAuthorValidator(bookRepo);
        BookDescriptionValidator v3 = new BookDescriptionValidator(bookRepo);
        BookGenresValidator v4 = new BookGenresValidator(bookRepo);
        BookSeriesValidator v5 = new BookSeriesValidator(bookRepo);
        BookTitleValidator v6 = new BookTitleValidator(bookRepo);
        v5.setNext(v6);
        v4.setNext(v5);
        v3.setNext(v4);
        v2.setNext(v3);
        bookHandler.setNext(v2);
        try{
            bookHandler.handle(newBook);
        }
        catch (InvalidBookException | InvalidAuthorException | InvalidBookIdException e){
            if(e.getMessage().equals("Book must have an id")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have an id");
            }
            if(e.getMessage().equals("Book must have an author")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have an author");
            }
            if(e.getMessage().equals("The author name contains illegal characters.")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The author name contains illegal characters.");
            }
            if(e.getMessage().equals("Book must have a description")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have a description");
            }
            if(e.getMessage().equals("Book must have at least one genre")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have at least one genre");
            }
            if(e.getMessage().equals("Book must have a series associated to it")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have a series associated to it");
            }
            if(e.getMessage().equals("Book must have a title")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have a title");
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(bookService.createBook(newBook));
    }
    @PutMapping("/{id}/{username}")
    public ResponseEntity updateBook(@PathVariable Long id, @PathVariable String username, @RequestBody Book updatedBook){

        BookExistingValidator bookHandler = new BookExistingValidator(bookRepo);
        UserExistingValidator userHandler = new UserExistingValidator(userRepo);
        AdminValidator av = new AdminValidator(userRepo);
        userHandler.setNext(av);

        User user = new User();
        user.setUsername(username);

        Book book = new Book();
        book.setId(id);

        try {
            userHandler.handle(user);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e){
            if(e.getMessage().equals("User does not exist")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
            }
            if(e.getMessage().equals("User is not an admin")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an admin");
            }
        }

        try {
            bookHandler.handle(book);
        }
        catch (InvalidBookException | InvalidAuthorException | InvalidBookIdException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This book does not exist");
        }

        BookIdValidator bookHandler2 = new BookIdValidator(bookRepo);
        BookAuthorValidator v2 = new BookAuthorValidator(bookRepo);
        BookDescriptionValidator v3 = new BookDescriptionValidator(bookRepo);
        BookGenresValidator v4 = new BookGenresValidator(bookRepo);
        BookSeriesValidator v5 = new BookSeriesValidator(bookRepo);
        BookTitleValidator v6 = new BookTitleValidator(bookRepo);
        v5.setNext(v6);
        v4.setNext(v5);
        v3.setNext(v4);
        v2.setNext(v3);
        bookHandler2.setNext(v2);
        try{
            bookHandler2.handle(updatedBook);
        }
        catch (InvalidBookException | InvalidAuthorException | InvalidBookIdException e){
            if(e.getMessage().equals("Book must have an id")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have an id");
            }
            if(e.getMessage().equals("Book must have an author")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have an author");
            }
            if(e.getMessage().equals("The author name contains illegal characters.")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The author name contains illegal characters.");
            }
            if(e.getMessage().equals("Book must have a description")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have a description");
            }
            if(e.getMessage().equals("Book must have at least one genre")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have at least one genre");
            }
            if(e.getMessage().equals("Book must have a series associated to it")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have a series associated to it");
            }
            if(e.getMessage().equals("Book must have a title")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have a title");
            }

        }

        bookService.updateBook(updatedBook, id);

        return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity getBook(@PathVariable Long id){

        BookExistingValidator bookHandler = new BookExistingValidator(bookRepo);
        BookIdValidator bookIdValidator = new BookIdValidator(bookRepo);
        bookHandler.setNext(bookIdValidator);
        Book book = new Book();
        book.setId(id);
        try {
            bookHandler.handle(book);
        }
        catch(InvalidBookException | InvalidBookIdException | InvalidAuthorException e){
            if(e.getMessage().equals("Book does not exist")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This book does not exist");
            }
            if(e.getMessage().equals("Book must have an id")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have an id");
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBook(id));
    }

    @DeleteMapping("/{id}/{username}")
    public ResponseEntity deleteBook(@PathVariable Long id, @PathVariable String username){

        UserExistingValidator userHandler = new UserExistingValidator(userRepo);
        AdminValidator av = new AdminValidator(userRepo);
        userHandler.setNext(av);
        BookExistingValidator bookHandler = new BookExistingValidator(bookRepo);
        BookIdValidator bookIdValidator = new BookIdValidator(bookRepo);
        bookHandler.setNext(bookIdValidator);
        User user = new User();
        user.setUsername(username);
        Book book = new Book();
        book.setId(id);
        try {
            userHandler.handle(user);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e){
            if(e.getMessage().equals("User does not exist")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
            }
            if(e.getMessage().equals("User is not an admin")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an admin");
            }
        }
        try {
            bookHandler.handle(book);
        }
        catch (InvalidBookException | InvalidAuthorException | InvalidBookIdException e){
            if(e.getMessage().equals("Book does not exist")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book does not exist");
            }
            if(e.getMessage().equals("Book must have an id")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have an id");
            }
        }

        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted succesfully");
    }

    @PutMapping("/read/{id}")
    public ResponseEntity readBook(@PathVariable Long id){

        BookExistingValidator bookHandler = new BookExistingValidator(bookRepo);
        BookIdValidator bookIdValidator = new BookIdValidator(bookRepo);
        bookHandler.setNext(bookIdValidator);
        Book book = new Book();
        book.setId(id);
        try {
            bookHandler.handle(book);
        }
        catch (InvalidBookException | InvalidAuthorException | InvalidBookIdException e){
            if(e.getMessage().equals("Book does not exist")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book does not exist");
            }
            if(e.getMessage().equals("Book must have an id")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have an id");
            }
        }

        bookService.readBook(id);
        return ResponseEntity.status(HttpStatus.OK).body("Book successfully read");
    }

    @GetMapping("")
    public ResponseEntity getBooks(@RequestBody List<String> ids) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBooks(ids));
    }

    @GetMapping("/search")
    public ResponseEntity bookSearchGet(
        String author,
        String genre,
        String title,
        String description,
        String series,
        String sortBy
    ) {
        Book book = new Book();
        List<String> genres = new ArrayList<>();
        genres.add(genre);
        book.setGenres(genres);
        book.setAuthor(author);
        book.setTitle(title);
        book.setDescription(description);
        book.setSeries(series);
        BookAuthorValidator bookHandler = new BookAuthorValidator(bookRepo);
        BookTitleValidator v2 = new BookTitleValidator(bookRepo);
        BookDescriptionValidator v3 = new BookDescriptionValidator(bookRepo);
        BookSeriesValidator v4 = new BookSeriesValidator(bookRepo);
        BookGenresValidator v5 = new BookGenresValidator(bookRepo);
        v4.setNext(v5);
        v3.setNext(v4);
        v2.setNext(v3);
        bookHandler.setNext(v2);
        try {
            bookHandler.handle(book);
        }
        catch (InvalidBookException | InvalidAuthorException | InvalidBookIdException e){
            if(e.getMessage().equals("Book must have an author")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have an author");
            }
            if(e.getMessage().equals("The author name contains illegal characters.")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The author name contains illegal characters.");
            }
            if(e.getMessage().equals("Book must have a description")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have a description");
            }
            if(e.getMessage().equals("Book must have at least one genre")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have at least one genre");
            }
            if(e.getMessage().equals("Book must have a series associated to it")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have a series associated to it");
            }
            if(e.getMessage().equals("Book must have a title")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book must have a title");
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(bookService.findBook(author, genre, title, description, series, sortBy));
    }
}
