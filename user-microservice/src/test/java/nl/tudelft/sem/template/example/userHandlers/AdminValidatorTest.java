package nl.tudelft.sem.template.example.userHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.Test;

class AdminValidatorTest {

    @Test
    void testHandleAdminUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User adminUser = new User();
        adminUser.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);

        AdminValidator adminValidator = new AdminValidator(userRepository);

        boolean result = adminValidator.handle(adminUser);

        assertTrue(result);
    }

    @Test
    void testHandleNonAdminUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        UserRepository userRepository = mock(UserRepository.class);

        User user = new User();
        user.getUserStatus().setUserRole(User.UserRoleEnum.REGULAR);

        AdminValidator adminValidator = new AdminValidator(userRepository);

        assertThrows(InvalidUserException.class, () -> adminValidator.handle(user));
    }
}