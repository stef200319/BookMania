package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Book;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.BookService;
import nl.tudelft.sem.template.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookControllerTest {

    private UserRepository userRepository;
    private BookRepository bookRepository;
    private BookService bookService;
    private BookController bookController;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = Mockito.mock(BookService.class);
        bookController = new BookController(bookRepository, bookService);
    }

    /*
    @Test
    public void testCreateNullBook() {
        Book nullBook = null;
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));

        ResponseEntity response = bookController.createBook("testUsername", nullBook);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("This book does not exist", response.getBody());
    }

     */

}
