package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.authenticationStrategy.AdminAuthentication;
import nl.tudelft.sem.template.example.authenticationStrategy.Authenticate;
import nl.tudelft.sem.template.example.authenticationStrategy.AuthorAuthentication;
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
    private UserService userService;
    private BookRepository bookRepository;
    private BookService bookService;
    private BookController bookController;
    private AuthorAuthentication authenticator;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = Mockito.mock(BookService.class);
        authenticator = Mockito.mock(AuthorAuthentication.class);
        userService = Mockito.mock(UserService.class);
        bookController = new BookController(bookRepository, bookService, userRepository,userService,authenticator);
    }

    @Test
    public void testCreateNullBook() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book nullBook = null;
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        adminUser.getUserStatus().setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);
        ResponseEntity response = bookController.createBook("testUsername", nullBook);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
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
        adminUser.getUserStatus().setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }
    @Test
    public void testCreateInvalidBookID() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
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
        adminUser.getUserStatus().setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have an id", response.getBody());
    }

    @Test
    public void testCreateInvalidBookAuthorNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
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
        adminUser.getUserStatus().setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have an author", response.getBody());
    }
    @Test
    public void testCreateInvalidBookAuthor() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("]]]]]]");
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
        adminUser.getUserStatus().setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The author name contains illegal characters.", response.getBody());
    }

    @Test
    public void testCreateInvalidBookDescNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        adminUser.getUserStatus().setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a description", response.getBody());
    }

    @Test
    public void testCreateInvalidBookDescEmpty() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        book.setDescription("");
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
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a description", response.getBody());
    }

    @Test
    public void testCreateInvalidBookGenres() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have at least one genre", response.getBody());
    }

    @Test
    public void testCreateInvalidBookSeriesNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setTitle("A Story Story");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a series associated to it", response.getBody());
    }

    @Test
    public void testCreateInvalidBookSeriesEmpty() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setSeries("");
        book.setTitle("A Story Story");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a series associated to it", response.getBody());
    }

    @Test
    public void testCreateInvalidBookTitleEmpty() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a title", response.getBody());
    }

    @Test
    public void testCreateInvalidBookTitleNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setSeries("The Story Series");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a title", response.getBody());
    }

    @Test
    public void testCreateNonAdmin() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
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
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.REGULAR);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookService.createBook(book)).thenReturn(book);
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.createBook("testUsername", book);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not an admin", response.getBody());
    }

    @Test
    public void testUpdateValid() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
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
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book updated successfully", response.getBody());
    }

    @Test
    public void testUpdateInvalidId() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
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
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have an id", response.getBody());
    }

    @Test
    public void testUpdateInvalidAuthorEmpty() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("");
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
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have an author", response.getBody());
    }

    @Test
    public void testUpdateInvalidAuthorNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
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
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have an author", response.getBody());
    }

    @Test
    public void testUpdateInvalidDescEmpty() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        book.setDescription("");
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
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a description", response.getBody());
    }
    @Test
    public void testUpdateInvalidDescNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
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
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a description", response.getBody());
    }

    @Test
    public void testUpdateInvalidGenres() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have at least one genre", response.getBody());
    }

    @Test
    public void testUpdateInvalidSeriesEmpty() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setSeries("");
        book.setTitle("A Story Story");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a series associated to it", response.getBody());
    }

    @Test
    public void testUpdateInvalidSeriesNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setTitle("A Story Story");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a series associated to it", response.getBody());
    }

    @Test
    public void testUpdateInvalidTitleEmpty() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a title", response.getBody());
    }

    @Test
    public void testUpdateInvalidTitleNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Bob Authorson");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setDescription("This book contains story");
        book.setReads(0L);
        book.setSeries("The Story Series");
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have a title", response.getBody());
    }

    @Test
    public void testUpdateNonAdmin() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
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
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.REGULAR);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.updateBook(1L, "testUsername", book);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not an admin", response.getBody());
    }

    @Test
    public void testGetValid() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        Long id = 1L;
        book.setAuthor("Bob Authorson");
        book.setDescription("This book contains story");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");

        Mockito.when(bookRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Mockito.when(bookService.getBook(id)).thenReturn(book);

        ResponseEntity response = bookController.getBook(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    public void testGetNullId() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        Long id = null;
        book.setAuthor("Bob Authorson");
        book.setDescription("This book contains story");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");

        ResponseEntity response = bookController.getBook(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have an id", response.getBody());
    }

    @Test
    public void testGetInvalidId() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        Long id = 1L;
        book.setAuthor("Bob Authorson");
        book.setDescription("This book contains story");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");

        Mockito.when(bookRepository.existsById(id)).thenReturn(false);

        ResponseEntity response = bookController.getBook(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("This book does not exist", response.getBody());
    }

    @Test
    public void testDeleteValid() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(true);

        ResponseEntity response = bookController.deleteBook(id, "testUsername");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book deleted succesfully", response.getBody());
    }

    @Test
    public void testDeleteIdNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(true);

        ResponseEntity response = bookController.deleteBook(null, "testUsername");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have an id", response.getBody());
    }
    @Test
    public void testDeleteIdNotInRepo() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(true);

        ResponseEntity response = bookController.deleteBook(1L, "testUsername");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book does not exist", response.getBody());
    }

    @Test
    public void testDeleteNonAdmin() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        User adminUser = new User();
        adminUser.setUsername("testUsername");
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.REGULAR);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(adminUser));
        Mockito.when(bookRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Mockito.when(authenticator.auth(adminUser.getUsername())).thenReturn(false);

        ResponseEntity response = bookController.deleteBook(id, "testUsername");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not an admin", response.getBody());
    }

    @Test
    public void testReadValid() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        Long id = 1L;
        book.setAuthor("Bob Authorson");
        book.setDescription("This book contains story");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");

        Mockito.when(bookRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        ResponseEntity response = bookController.readBook(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book successfully read", response.getBody());
    }

    @Test
    public void testReadInvalidId() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        Long id = 1L;
        book.setAuthor("Bob Authorson");
        book.setDescription("This book contains story");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");

        Mockito.when(bookRepository.existsById(id)).thenReturn(false);

        ResponseEntity response = bookController.readBook(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book does not exist", response.getBody());
    }

    @Test
    public void testReadIdNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        ResponseEntity response = bookController.readBook(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book must have an id", response.getBody());
    }

    @Test
    public void testGetBooksValid() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        Book book = new Book();
        book.setId(1L);
        Long id = 1L;
        List<String> ids = new LinkedList<String>();
        ids.add("1");
        book.setAuthor("Bob Authorson");
        book.setDescription("This book contains story");
        LinkedList genres = new LinkedList<String>();
        genres.add("Novel");
        book.setGenres(genres);
        book.setReads(0L);
        book.setSeries("The Story Series");
        book.setTitle("A Story Story");

        List<Book> books = new LinkedList<Book>();
        books.add(book);

        Mockito.when(bookRepository.existsById(id)).thenReturn(true);
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.getOne(id)).thenReturn(book);
        Mockito.when(bookService.getBooks(ids)).thenReturn(books);

        ResponseEntity response = bookController.getBooks(ids);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

}
