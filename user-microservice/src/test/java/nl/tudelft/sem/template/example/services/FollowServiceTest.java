package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FollowServiceTest {

    private UserRepository userRepository;
    private AnalyticsService analyticsService;
    private FollowService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        analyticsService = Mockito.mock(AnalyticsService.class);

        userService = new FollowService(userRepository, analyticsService);
    }

    @Test
    public void followUserTest() {
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        Analytics a1 = new Analytics("user1");
        Analytics a2 = new Analytics("user2");
        Mockito.when(analyticsService.getAnalytics("user1")).thenReturn(a1);
        Mockito.when(analyticsService.getAnalytics("user2")).thenReturn(a2);

        User newUser1 = userService.followUser(user1, user2);

        List<String> ans = new LinkedList<>();
        ans.add(user2.getUsername());

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

        List<String> following = new LinkedList<>();
        following.add(user2.getUsername());
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

        List<String> following = new LinkedList<>();
        following.add(user2.getUsername());
        user1.setFollowing(following);

        List<String> followers = new LinkedList<>();
        followers.add(user1.getUsername());
        user2.setFollowers(followers);

        Analytics a1 = new Analytics("user1");
        a1.setFollowingNumber(1L);
        Analytics a2 = new Analytics("user2");
        a2.setFollowersNumber(1L);

        Mockito.when(analyticsService.getAnalytics("user1")).thenReturn(a1);
        Mockito.when(analyticsService.getAnalytics("user2")).thenReturn(a2);

        List<String> ans = new LinkedList<>();

        User newUser1 = userService.unfollowUser(user1, user2);

        assertEquals(ans, newUser1.getFollowing());
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(user1);
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(user2);
    }

    @Test
    public void testGetFollowing() {
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

        assertEquals(ans, userService.getFollowing(user));
    }

    @Test
    public void testGetFollowingEmpty() {
        User user = new User();
        user.setUsername("user");

        List<User> ans = new LinkedList<>();

        Mockito.when(userRepository.existsById("user")).thenReturn(true);
        Mockito.when(userRepository.findById("user")).thenReturn(Optional.of(user));

        assertEquals(ans, userService.getFollowing(user));
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

        assertEquals(ans, userService.getFollowers(user));
    }

    @Test
    public void testGetFollowersEmpty() {
        User user = new User();
        user.setUsername("user");

        List<User> ans = new LinkedList<>();

        Mockito.when(userRepository.existsById("user")).thenReturn(true);
        Mockito.when(userRepository.findById("user")).thenReturn(Optional.of(user));

        assertEquals(ans, userService.getFollowers(user));
    }
}
