package nl.tudelft.sem.template.example.authenticators;

import nl.tudelft.sem.template.example.authenticationStrategy.UserAuthentication;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.UserService;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class UserAuthenticationTest {
    @Test
    public void testNotValidUsername(){
        UserService userService = Mockito.mock(UserService.class);
        String username = "username";
        UserAuthentication userAuthentication = new UserAuthentication(userService,username);
        Mockito.when(userService.fetchUser(username)).thenThrow(NoSuchElementException.class);
        assertFalse(userAuthentication.auth());
    }
    @Test
    public void testUserIsAdmin(){
        UserService userService = Mockito.mock(UserService.class);
        String username = "username";
        UserAuthentication userAuthentication = new UserAuthentication(userService,username);
        User user = new User();
        UserStatus userStatus = new UserStatus();
        userStatus.setUserRole(User.UserRoleEnum.ADMIN);
        user.setUserStatus(userStatus);
        Mockito.when(userService.fetchUser(username)).thenReturn(user);
        assertFalse(userAuthentication.auth());
    }
    @Test
    public void testUserIsRegular(){
        UserService userService = Mockito.mock(UserService.class);
        String username = "username";
        UserAuthentication userAuthentication = new UserAuthentication(userService,username);
        User user = new User();
        UserStatus userStatus = new UserStatus();
        userStatus.setUserRole(User.UserRoleEnum.REGULAR);
        user.setUserStatus(userStatus);
        Mockito.when(userService.fetchUser(username)).thenReturn(user);
        assertTrue(userAuthentication.auth());

    }
    @Test
    public void testUserIsAuthor(){
        UserService userService = Mockito.mock(UserService.class);
        String username = "username";
        UserAuthentication userAuthentication = new UserAuthentication(userService,username);
        User user = new User();
        UserStatus userStatus = new UserStatus();
        userStatus.setUserRole(User.UserRoleEnum.AUTHOR);
        user.setUserStatus(userStatus);
        Mockito.when(userService.fetchUser(username)).thenReturn(user);
        assertTrue(userAuthentication.auth());
    }

}


