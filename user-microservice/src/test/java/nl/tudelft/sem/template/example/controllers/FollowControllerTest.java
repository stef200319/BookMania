package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.authenticationStrategy.AdminAuthentication;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FollowControllerTest {
    private UserRepository userRepository;
    private UserService userService;
    private FollowController userController;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = Mockito.mock(UserService.class);
        userController = new FollowController(userRepository, userService);
    }

    @Test
    public void testFollowInvalidFirstUser() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(false);
        Mockito.when(userRepository.existsById("user2")).thenReturn(true);

        Mockito.when(userRepository.findById("user2")).thenReturn(Optional.of(user2));

        ResponseEntity response = userController.followUser("user1", "user2");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username of the user executing the action is not valid", response.getBody());

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

        Mockito.when(userRepository.findById("user1")).thenReturn(Optional.of(user1));

        ResponseEntity response = userController.followUser("user1", "user2");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username of the user being followed is not valid", response.getBody());

        Mockito.verify(userService, Mockito.times(0)).followUser(user1, user2);
    }

    @Test
    public void testFollowNotLoggedIn() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.getUserStatus().setIsLoggedIn(false);

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
        user1.getUserStatus().setIsLoggedIn(true);

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
        user1.getUserStatus().setIsLoggedIn(true);

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(true);
        Mockito.when(userRepository.existsById("user2")).thenReturn(false);

        Mockito.when(userRepository.findById("user1")).thenReturn(Optional.of(user1));

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
        user1.getUserStatus().setIsLoggedIn(false);

        User user2 = new User();
        user2.setUsername("user2");

        Mockito.when(userRepository.existsById("user1")).thenReturn(true);
        Mockito.when(userRepository.existsById("user2")).thenReturn(true);

        Mockito.when(userRepository.findById("user1")).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById("user2")).thenReturn(Optional.of(user2));

        ResponseEntity response = userController.unfollowUser("user1", "user2");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not logged in", response.getBody());

        Mockito.verify(userService, Mockito.times(0)).unfollowUser(user1, user2);
    }

    @Test
    public void testUnfollowNotFollowing() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.getUserStatus().setIsLoggedIn(true);

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
        user1.getUserStatus().setIsLoggedIn(true);

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
        user.setUsername("user");
        user2.setUsername("test");

        List<String> foll = new LinkedList<>();
        foll.add(user2.getUsername());

        user.setFollowing(foll);

        List<User> ans = new LinkedList<>();
        ans.add(user2);

        Mockito.when(userRepository.existsById("user")).thenReturn(true);
        Mockito.when(userRepository.findById("user")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsById("test")).thenReturn(true);
        Mockito.when(userRepository.findById("test")).thenReturn(Optional.of(user2));
        Mockito.when(userService.getFollowing(user)).thenReturn(ans);

        ResponseEntity response = userController.getFollowing("user");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ans, response.getBody());
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

        List<String> foll = new LinkedList<>();
        foll.add("test");

        List<User> ans = new LinkedList<>();
        ans.add(user2);

        user.setUsername("user");
        user.setFollowers(foll);

        Mockito.when(userRepository.existsById("user")).thenReturn(true);
        Mockito.when(userRepository.findById("user")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsById("test")).thenReturn(true);
        Mockito.when(userRepository.findById("test")).thenReturn(Optional.of(user2));
        Mockito.when(userService.getFollowers(user)).thenReturn(ans);

        ResponseEntity response = userController.getFollowers("user");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ans, response.getBody());
    }
}
