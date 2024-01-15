package nl.tudelft.sem.template.example.userHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.Test;

class UserActiveValidatorTest {

    @Test
    void testActiveUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.getUserStatus().setIsActive(true);

        UserActiveValidator validator = new UserActiveValidator(userRepository);

        boolean result = validator.handle(user);

        assertTrue(result);
    }

    @Test
    void testInactiveUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.getUserStatus().setIsActive(false);

        UserActiveValidator validator = new UserActiveValidator(userRepository);

        assertThrows(InvalidUserException.class, () -> validator.handle(user));
    }
}

