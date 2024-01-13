package nl.tudelft.sem.template.example.userHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class UserExistingValidatorTest {

    @Test
    void testExistingUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.setUsername("test");

        when(userRepository.existsById("test")).thenReturn(true);
        when(userRepository.findById("test")).thenReturn(Optional.of(user));

        UserExistingValidator validator = new UserExistingValidator(userRepository);

        boolean result = validator.handle(user);

        assertTrue(result);
    }

    @Test
    void testNonExistentUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.setUsername("test");

        when(userRepository.existsById("test")).thenReturn(false);

        UserExistingValidator validator = new UserExistingValidator(userRepository);

        assertThrows(InvalidUserException.class, () -> validator.handle(user));
    }
}



