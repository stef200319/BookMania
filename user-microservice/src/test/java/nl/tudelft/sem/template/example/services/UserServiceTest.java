package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void followUserTest() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        User newUser1 = userService.followUser(user1, user2);

        List<User> ans = new LinkedList<>();
        ans.add(user2);

        assertEquals(ans, newUser1.getFollowing());
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(user1);
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(user2);
    }

    @Test
    public void unfollowUserNotFollowingTest() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        User newUser1 = userService.unfollowUser(user1, user2);

        assertNull(newUser1);
        Mockito.verify(userRepository, Mockito.times(0)).saveAndFlush(user1);
        Mockito.verify(userRepository, Mockito.times(0)).saveAndFlush(user2);
    }

    @Test
    public void unfollowUserNotFollowedTest() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        List<User> following = new LinkedList<>();
        following.add(user2);
        user1.setFollowing(following);

        User newUser1 = userService.unfollowUser(user1, user2);

        assertNull(newUser1);
        Mockito.verify(userRepository, Mockito.times(0)).saveAndFlush(user1);
        Mockito.verify(userRepository, Mockito.times(0)).saveAndFlush(user2);
    }

    @Test
    public void unfollowUserWorking() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        List<User> following = new LinkedList<>();
        following.add(user2);
        user1.setFollowing(following);

        List<User> followers = new LinkedList<>();
        followers.add(user1);
        user2.setFollowers(followers);

        List<User> ans = new LinkedList<>();

        User newUser1 = userService.unfollowUser(user1, user2);

        assertEquals(ans, newUser1.getFollowing());
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(user1);
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(user2);
    }
}
