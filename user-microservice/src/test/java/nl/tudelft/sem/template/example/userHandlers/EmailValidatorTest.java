package nl.tudelft.sem.template.example.userHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {

    @Test
    void testGoodEmail() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.setEmail("abc@fdg.com");

        EmailValidator emailValidator = new EmailValidator(userRepository);

        boolean result = emailValidator.handle(user);

        assertTrue(result);
    }

    @Test
    void testInvalidEmail() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.setEmail("sad");

        EmailValidator emailValidator = new EmailValidator(userRepository);

        assertThrows(InvalidEmailException.class, () -> emailValidator.handle(user));
    }
}
