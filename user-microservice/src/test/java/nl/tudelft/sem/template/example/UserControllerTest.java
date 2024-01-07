package nl.tudelft.sem.template.example;

import nl.tudelft.sem.template.example.controllers.UserController;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class UserControllerTest {

    private UserRepository userRepository;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userController = new UserController(userRepository);
    }
    @Test
    public void testInvalidLogInWithInvalidUsername() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(false);

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
    }

    @Test
    public void testInvalidLogInWithInvalidIsActive() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(false);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User account is not active", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testAlreadyLoggedIn() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User already logged in", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testValidLogIn() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged in successfully", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testInvalidLogOutWithInvalidUsername() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(false);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logOutUser("testUsername");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(0)).findById("testUsername");
    }

    @Test
    public void testInvalidLogOutWithNullLoggedIn() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(null);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logOutUser("testUsername");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not logged in", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testInvalidLogOutWithFalseLoggedIn() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(false);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logOutUser("testUsername");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not logged in", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testValidLogOut() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logOutUser("testUsername");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged out successfully", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }
}
