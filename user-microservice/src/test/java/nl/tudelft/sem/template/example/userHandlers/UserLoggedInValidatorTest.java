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
public class UserLoggedInValidatorTest {
    private UserLoggedInValidator userLoggedInValidator;
    private UserRepository userRepositoryMock;

    @BeforeEach
    public void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        userLoggedInValidator = new UserLoggedInValidator(userRepositoryMock);
    }

    @Test
    public void testHandleUserIsLoggedIn() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        when(user.getIsLoggedIn()).thenReturn(true);

        assertTrue(userLoggedInValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }

    @Test
    public void testHandleUserIsNotLoggedIn() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        when(user.getIsLoggedIn()).thenReturn(false);

        assertThrows(InvalidUserException.class, () -> userLoggedInValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }

    @Test
    public void testHandleUserLoggedInIsNull() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        when(user.getIsLoggedIn()).thenReturn(null);

        assertThrows(InvalidUserException.class, () -> userLoggedInValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }
}
