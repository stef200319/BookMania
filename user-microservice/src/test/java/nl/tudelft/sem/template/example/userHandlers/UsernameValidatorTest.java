package nl.tudelft.sem.template.example.userHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class UsernameValidatorTest {
    private UsernameValidator usernameValidator;
    private UserRepository userRepositoryMock;

    @BeforeEach
    public void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        usernameValidator = new UsernameValidator(userRepositoryMock);
    }

    @Test
    public void testHandleValidUsername() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("validUsername");
        when(userRepositoryMock.existsById("validUsername")).thenReturn(false);

        assertTrue(usernameValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }

    @Test
    public void testHandleUsernameAlreadyInUse() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("existingUsername");
        when(userRepositoryMock.existsById("existingUsername")).thenReturn(true);

        assertThrows(InvalidUsernameException.class, () -> usernameValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }

    @Test
    public void testHandleInvalidUsernameFormat() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("invalid-username");

        assertThrows(InvalidUsernameException.class, () -> usernameValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }
}
