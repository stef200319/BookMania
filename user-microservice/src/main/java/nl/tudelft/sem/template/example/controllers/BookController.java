package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.model.Book;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    UserRepository userRepo;
    BookRepository bookRepo;

    @PostMapping("/{username}")
    public ResponseEntity createBook(@RequestBody Book newBook){
        /*
        if(userRepo.findByUsername()){
            logic for determining if the user is even allowed to make a book WILL GO HERE
        }
         */

        Book savedBook = bookRepo.saveAndFlush(newBook);
        return ResponseEntity.status(HttpStatus.OK).body(savedBook);
    }
    @PutMapping("/{id}/{username}")
    public ResponseEntity updateBook(@PathVariable Long id, @RequestBody Book updatedBook){

        /*
        if(userRepo.findByUsername()){
            logic for determining if the user is even allowed to make a book WILL GO HERE
        }
         */

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

}
