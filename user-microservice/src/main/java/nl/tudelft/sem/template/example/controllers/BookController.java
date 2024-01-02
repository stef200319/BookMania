package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.model.Book;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.database.UserRepository;
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
    BookRepository bookRepo;

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

        Book savedBook = bookRepo.saveAndFlush(newBook);
        return ResponseEntity.status(HttpStatus.OK).body(savedBook);
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

        // Fetch book from database
        Book opBook = this.bookRepo.getOne(id);

        // Update the book
        opBook.setAuthor(updatedBook.getAuthor());
        opBook.setTitle(updatedBook.getTitle());
        opBook.setDescription(updatedBook.getDescription());
        opBook.setReads(updatedBook.getReads());
        opBook.setSeries(updatedBook.getSeries());
        opBook.setGenres(updatedBook.getGenres());

        // Save the changes
        this.bookRepo.saveAndFlush(opBook);
        return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity getBook(@PathVariable Long id){

        if(!bookRepo.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This book does not exist");
        }

        Book bookToGet = bookRepo.getOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookToGet);
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

        this.bookRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted succesfully");
    }

    @PutMapping("/read/{id}")
    public ResponseEntity readBook(@PathVariable Long id){

        if(!bookRepo.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This book does not exist");
        }

        Book bookToRead = bookRepo.getOne(id);
        bookToRead.setReads(bookToRead.getReads() + 1);
        this.bookRepo.saveAndFlush(bookToRead);
        return ResponseEntity.status(HttpStatus.OK).body("Book successfully read");
    }

    @GetMapping("")
    public ResponseEntity getBooks(@RequestBody List<String> ids) {

        List<Book> books = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            try {
                Long id = Long.parseLong(ids.get(i));
                if (bookRepo.existsById(id)) {
                    books.add(bookRepo.getOne(id));
                }
            }
            catch (NumberFormatException e) {
                System.out.println("invalid id");
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }
}
