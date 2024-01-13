package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void testUpdateUserInfo(){
        User currentUser = new User();
        currentUser.setUsername("test");
        currentUser.setIsLoggedIn(true);
        currentUser.setEmail("email@tud.com");

        User modifiedUser = new User();
        modifiedUser.setUsername("test");
        modifiedUser.setIsLoggedIn(true);
        modifiedUser.setEmail("email2@tud.com");
        modifiedUser.setFirstName("bob");
        modifiedUser.setLastName("bobby");
        modifiedUser.setBio("nice guy");
        modifiedUser.setProfilePicture("pic");
        modifiedUser.setLocation("location");
        modifiedUser.setPassword("pass");
        modifiedUser.setFavoriteBook("Crime and Punishment");
        modifiedUser.setFavoriteGenres(new ArrayList<>());
        Mockito.when(userRepository.findById(modifiedUser.getUsername())).thenReturn(Optional.of(currentUser));
        userService.updateUserInfo(modifiedUser);
        assertEquals(currentUser.getEmail(),modifiedUser.getEmail());
        assertEquals(currentUser.getFirstName(),modifiedUser.getFirstName());
        assertEquals(currentUser.getLastName(),modifiedUser.getLastName());
        assertEquals(currentUser.getBio(),modifiedUser.getBio());
        assertEquals(currentUser.getProfilePicture(),modifiedUser.getProfilePicture());
        assertEquals(currentUser.getLocation(),modifiedUser.getLocation());
        assertEquals(currentUser.getPassword(),modifiedUser.getPassword());
        assertEquals(currentUser.getFavoriteBook(),modifiedUser.getFavoriteBook());
        assertEquals(currentUser.getFavoriteGenres(),modifiedUser.getFavoriteGenres());
        Mockito.verify(userRepository,Mockito.times(1)).saveAndFlush(currentUser);
    }

    @Test
    public void testModifyActivationStatus(){
        User user = new User();
        user.setUsername("test");
        user.setIsActive(false);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        userService.modifyUserActivationStatus(user.getUsername(),true);
        assertEquals(user.getIsActive(),true);
        Mockito.verify(userRepository,Mockito.times(1)).saveAndFlush(user);
    }

    @Test
    public void testFetchUser(){
        User user = new User();
        user.setUsername("test");
        user.setUserRole(User.UserRoleEnum.REGULAR);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        User fetchedUser = userService.fetchUser(user.getUsername());
        assertEquals(user.getUserRole(),fetchedUser.getUserRole());
        Mockito.verify(userRepository,Mockito.times(1)).findById(user.getUsername());
    }

    @Test
    public void testDeleteUser(){
        User user = new User();
        user.setUsername("test");
        userService.deleteUser(user.getUsername());
        Mockito.verify(userRepository,Mockito.times(1)).deleteById(user.getUsername());
    }

}
