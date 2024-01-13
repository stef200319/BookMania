package nl.tudelft.sem.template.example.userHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    @Test
    void testGoodPassword() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.setPassword("123");

        User loggingIn = new User();
        loggingIn.setPassword("123");

        PasswordValidator passwordValidator = new PasswordValidator(userRepository, loggingIn);

        boolean result = passwordValidator.handle(user);

        assertTrue(result);
    }

    @Test
    void testInvalidPassword() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.setPassword("123");

        User loggingIn = new User();
        loggingIn.setPassword("1234");

        PasswordValidator passwordValidator = new PasswordValidator(userRepository, loggingIn);

        assertThrows(InvalidUserException.class, () -> passwordValidator.handle(user));
    }
}
