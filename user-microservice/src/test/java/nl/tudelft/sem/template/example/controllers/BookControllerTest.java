package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.Book;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.BookService;
import nl.tudelft.sem.template.example.services.UserService;
import nl.tudelft.sem.template.example.userHandlers.UserExistingValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
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
    private UserExistingValidator userExisting;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = Mockito.mock(BookService.class);
        bookController = new BookController(bookRepository, bookService, userRepository);
    }

    @Test
    public void testCreateNullBook() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book nullBook = null;
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));

        ResponseEntity response = bookController.createBook("testUsername", nullBook);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book cannot be null", response.getBody());
    }

    @Test
    public void testCreateValidBook() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        book.setDescription("This book contains story");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

}
