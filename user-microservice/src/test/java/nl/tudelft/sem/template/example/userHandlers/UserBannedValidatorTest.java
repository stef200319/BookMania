package nl.tudelft.sem.template.example.userHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.Test;

class UserBannedValidatorTest {

    @Test
    void testNotBannedUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.getUserStatus().setIsBanned(false);

        UserBannedValidator validator = new UserBannedValidator(userRepository);

        boolean result = validator.handle(user);

        assertTrue(result);
    }

    @Test
    void testBannedUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.getUserStatus().setIsBanned(true);

        UserBannedValidator validator = new UserBannedValidator(userRepository);

        assertThrows(InvalidUserException.class, () -> validator.handle(user));
    }
}


