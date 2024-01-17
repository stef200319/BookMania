package nl.tudelft.sem.template.example.authenticators;

import nl.tudelft.sem.template.example.authenticationStrategy.AdminAuthentication;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.UserService;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class AdminAuthenticationTest {
    @Test
    public void testNotValidUsername() {
        UserService userService = Mockito.mock(UserService.class);
        String username = "username";
        AdminAuthentication adminAuthentication = new AdminAuthentication(userService);
        Mockito.when(userService.fetchUser(username)).thenThrow(NoSuchElementException.class);
        assertFalse(adminAuthentication.auth(username));
    }

    @Test
    public void testUserIsAdmin() {
        UserService userService = Mockito.mock(UserService.class);
        String username = "username";
        AdminAuthentication adminAuthentication = new AdminAuthentication(userService);
        User user = new User();
        UserStatus userStatus = new UserStatus();
        userStatus.setUserRole(User.UserRoleEnum.ADMIN);
        user.setUserStatus(userStatus);
        Mockito.when(userService.fetchUser(username)).thenReturn(user);
        assertEquals(user.getUserStatus().getUserRole().getValue(),"Admin");
        assertTrue(adminAuthentication.auth(username));
    }

    @Test
    public void testUserIsNotAdmin() {
        UserService userService = Mockito.mock(UserService.class);
        String username = "username";
        AdminAuthentication adminAuthentication = new AdminAuthentication(userService);
        User user = new User();
        UserStatus userStatus = new UserStatus();
        userStatus.setUserRole(User.UserRoleEnum.REGULAR);
        user.setUserStatus(userStatus);
        Mockito.when(userService.fetchUser(username)).thenReturn(user);
        assertFalse(adminAuthentication.auth(username));
    }
}
