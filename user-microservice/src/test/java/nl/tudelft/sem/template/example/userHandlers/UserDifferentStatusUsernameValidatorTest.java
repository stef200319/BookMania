package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.userUtilities.UserInfo;
import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDifferentStatusUsernameValidatorTest {

    private UserRepository userRepository;

    @BeforeEach()
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
    }

    @Test
    void sameUsername() {
        UserStatus status1 = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        UserProfile profile1 = new UserProfile("username","bio", "location", "picture", "favoriteBook", null);
        UserInfo info1 = new UserInfo("username", "Surname", "Name", "email", "password");
        User user1 = new User("username", info1, status1, profile1, null, null);
        UserDifferentStatusUsernameValidator h = new UserDifferentStatusUsernameValidator(userRepository);

        try {
            assertTrue(h.handle(user1));
        } catch (InvalidUserException e) {
            throw new RuntimeException(e);
        } catch (InvalidEmailException e) {
            throw new RuntimeException(e);
        } catch (InvalidUsernameException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void diffUsername() {
        UserStatus status1 = new UserStatus("username1", true, true, false, User.UserRoleEnum.REGULAR);
        UserProfile profile1 = new UserProfile("username1", "bio", "location", "picture", "favoriteBook", null);
        UserInfo info1 = new UserInfo("username", "Surname", "Name", "email", "password");
        User user1 = new User("username", info1, status1, profile1, null, null);
        UserDifferentProfileUsernameValidator h = new UserDifferentProfileUsernameValidator(userRepository);

        assertThrows(InvalidUserException.class, () -> h.handle(user1));
    }
}
