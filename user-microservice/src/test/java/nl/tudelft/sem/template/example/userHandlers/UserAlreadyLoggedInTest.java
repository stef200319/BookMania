package nl.tudelft.sem.template.example.userHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.Test;

class UserAlreadyLoggedInTest {

    @Test
    void testNotLoggedInUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.getUserStatus().setIsLoggedIn(false);

        UserAlreadyLoggedInValidator validator = new UserAlreadyLoggedInValidator(userRepository);

        boolean result = validator.handle(user);

        assertTrue(result);
    }

    @Test
    void testLoggedInUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.getUserStatus().setIsLoggedIn(true);

        UserAlreadyLoggedInValidator validator = new UserAlreadyLoggedInValidator(userRepository);

        assertThrows(InvalidUserException.class, () -> validator.handle(user));
    }
}
