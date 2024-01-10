package nl.tudelft.sem.template.example;

import nl.tudelft.sem.template.example.controllers.UserController;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;

import nl.tudelft.sem.template.example.services.UserService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UserControllerTest {

    private UserRepository userRepository;
    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userRepository, userService);
    }
    @Test
    public void testInvalidLogInWithInvalidUsername() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(false);

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
    }

    @Test
    public void testInvalidLogInWithInvalidIsActive() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(false);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User account is not active", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testAlreadyLoggedIn() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User already logged in", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testValidLogIn() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged in successfully", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testInvalidLogOutWithInvalidUsername() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(false);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logOutUser("testUsername");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(0)).findById("testUsername");
    }

    @Test
    public void testInvalidLogOutWithNullLoggedIn() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(null);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logOutUser("testUsername");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not logged in", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testInvalidLogOutWithFalseLoggedIn() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(false);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logOutUser("testUsername");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not logged in", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testValidLogOut() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");
        validUser.setIsActive(true);
        validUser.setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logOutUser("testUsername");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged out successfully", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testFollowInvalidFirstUser() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(false);

        ResponseEntity response = userController.followUser("user1", "user2");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username of the user executing the action is not valid", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("user1");
        Mockito.verify(userRepository, Mockito.times(0)).existsById("user2");
        Mockito.verify(userService, Mockito.times(0)).followUser(user1, user2);
    }

    @Test
    public void testFollowInvalidSecondUser() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(true);
        Mockito.when(userRepository.existsById("user2")).thenReturn(false);

        ResponseEntity response = userController.followUser("user1", "user2");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username of the user being followed is not valid", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("user1");
        Mockito.verify(userRepository, Mockito.times(1)).existsById("user2");
        Mockito.verify(userService, Mockito.times(0)).followUser(user1, user2);
    }

    @Test
    public void testFollowNotLoggedIn() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setIsLoggedIn(false);

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(true);
        Mockito.when(userRepository.existsById("user2")).thenReturn(true);

        Mockito.when(userRepository.findById("user1")).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById("user2")).thenReturn(Optional.of(user2));

        ResponseEntity response = userController.followUser("user1", "user2");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not logged in", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("user1");
        Mockito.verify(userRepository, Mockito.times(1)).existsById("user2");
        Mockito.verify(userService, Mockito.times(0)).followUser(user1, user2);
    }

    @Test
    public void testFollowWorking() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setIsLoggedIn(true);

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(true);
        Mockito.when(userRepository.existsById("user2")).thenReturn(true);

        Mockito.when(userRepository.findById("user1")).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById("user2")).thenReturn(Optional.of(user2));

        ResponseEntity response = userController.followUser("user1", "user2");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User account followed successfully", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("user1");
        Mockito.verify(userRepository, Mockito.times(1)).existsById("user2");
        Mockito.verify(userService, Mockito.times(1)).followUser(user1, user2);
    }

    @Test
    public void testUnfollowInvalidFirstUser() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(false);

        ResponseEntity response = userController.unfollowUser("user1", "user2");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username of the user executing the action is not valid", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("user1");
        Mockito.verify(userRepository, Mockito.times(0)).existsById("user2");
        Mockito.verify(userService, Mockito.times(0)).unfollowUser(user1, user2);
    }

    @Test
    public void testUnfollowInvalidSecondUser() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(true);
        Mockito.when(userRepository.existsById("user2")).thenReturn(false);

        ResponseEntity response = userController.unfollowUser("user1", "user2");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username of the user being unfollowed is not valid", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("user1");
        Mockito.verify(userRepository, Mockito.times(1)).existsById("user2");
        Mockito.verify(userService, Mockito.times(0)).unfollowUser(user1, user2);
    }

    @Test
    public void testUnfollowNotLoggedIn() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setIsLoggedIn(false);

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(true);
        Mockito.when(userRepository.existsById("user2")).thenReturn(true);

        Mockito.when(userRepository.findById("user1")).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById("user2")).thenReturn(Optional.of(user2));

        ResponseEntity response = userController.unfollowUser("user1", "user2");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not logged in", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("user1");
        Mockito.verify(userRepository, Mockito.times(1)).existsById("user2");
        Mockito.verify(userService, Mockito.times(0)).unfollowUser(user1, user2);
    }

    @Test
    public void testUnfollowNotFollowing() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setIsLoggedIn(true);

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(true);
        Mockito.when(userRepository.existsById("user2")).thenReturn(true);

        Mockito.when(userRepository.findById("user1")).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById("user2")).thenReturn(Optional.of(user2));

        Mockito.when(userService.unfollowUser(user1, user2)).thenReturn(null);

        ResponseEntity response = userController.unfollowUser("user1", "user2");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User does not follow the second user", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("user1");
        Mockito.verify(userRepository, Mockito.times(1)).existsById("user2");
        Mockito.verify(userService, Mockito.times(1)).unfollowUser(user1, user2);
    }

    @Test
    public void testUnfollowWorking() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setIsLoggedIn(true);

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(true);
        Mockito.when(userRepository.existsById("user2")).thenReturn(true);

        Mockito.when(userRepository.findById("user1")).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById("user2")).thenReturn(Optional.of(user2));

        Mockito.when(userService.unfollowUser(user1, user2)).thenReturn(user1);

        ResponseEntity response = userController.unfollowUser("user1", "user2");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User account unfollowed successfully", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("user1");
        Mockito.verify(userRepository, Mockito.times(1)).existsById("user2");
        Mockito.verify(userService, Mockito.times(1)).unfollowUser(user1, user2);
    }

    @Test
    public void testGetFollowingNotExistent() {
        Mockito.when(userRepository.existsById("user")).thenReturn(false);

        ResponseEntity response = userController.getFollowing("user");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username is not valid", response.getBody());
    }

    @Test
    public void testGetFollowingWorking() {
        User user = new User();
        User user2 = new User();
        user2.setUsername("test");

        List<User> foll = new LinkedList<>();
        foll.add(user2);

        user.setUsername("user");
        user.setFollowing(foll);

        Mockito.when(userRepository.existsById("user")).thenReturn(true);
        Mockito.when(userRepository.findById("user")).thenReturn(Optional.of(user));

        ResponseEntity response = userController.getFollowing("user");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(foll, response.getBody());
    }

    @Test
    public void testGetFollowersNotExistent() {
        Mockito.when(userRepository.existsById("user")).thenReturn(false);

        ResponseEntity response = userController.getFollowers("user");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username is not valid", response.getBody());
    }

    @Test
    public void testGetFollowersWorking() {
        User user = new User();
        User user2 = new User();
        user2.setUsername("test");

        List<User> foll = new LinkedList<>();
        foll.add(user2);

        user.setUsername("user");
        user.setFollowers(foll);

        Mockito.when(userRepository.existsById("user")).thenReturn(true);
        Mockito.when(userRepository.findById("user")).thenReturn(Optional.of(user));

        ResponseEntity response = userController.getFollowers("user");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(foll, response.getBody());
    }
}
