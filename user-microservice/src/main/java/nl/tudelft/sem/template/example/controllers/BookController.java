package nl.tudelft.sem.template.example.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import javax.validation.Valid;
import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.model.Book;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.services.BookService;
import nl.tudelft.sem.template.example.services.UserService;
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
        if(userRepo.findByUsername(username).isPresent()){
            User user = userRepo.findByUsername(username).get();
            if(!user.getUserRole().equals(User.UserRoleEnum.ADMIN)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(bookService.createBook(newBook));
    }
    @PutMapping("/{id}/{username}")
    public ResponseEntity updateBook(@PathVariable Long id, @PathVariable String username, @RequestBody Book updatedBook){

        if(userRepo.findByUsername(username).isPresent()){
            User user = userRepo.findByUsername(username).get();
            if(!user.getUserRole().equals(User.UserRoleEnum.ADMIN)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Check whether the book is already in the database
        if(!bookRepo.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This book does not exist");
        }

        bookService.updateBook(updatedBook, id);

        return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity getBook(@PathVariable Long id){

        if(!bookRepo.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This book does not exist");
        }

        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBook(id));
    }

    @DeleteMapping("/{id}/{username}")
    public ResponseEntity deleteBook(@PathVariable Long id, @PathVariable String username){
        if(userRepo.findByUsername(username).isPresent()){
            User user = userRepo.findByUsername(username).get();
            if(!user.getUserRole().equals(User.UserRoleEnum.ADMIN)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if(!bookRepo.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This book does not exist");
        }

        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted succesfully");
    }

    @PutMapping("/read/{id}")
    public ResponseEntity readBook(@PathVariable Long id){

        if(!bookRepo.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This book does not exist");
        }

        bookService.readBook(id);
        return ResponseEntity.status(HttpStatus.OK).body("Book successfully read");
    }

    @GetMapping("")
    public ResponseEntity getBooks(@RequestBody List<String> ids) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBooks(ids));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> bookSearchGet(
        String author,
        String genre,
        String title,
        String description,
        String series,
        String sortBy
    ) {
        if(author == null && genre == null && title == null && description == null && series == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findBook(author, genre, title, description, series, sortBy));
    }
}
