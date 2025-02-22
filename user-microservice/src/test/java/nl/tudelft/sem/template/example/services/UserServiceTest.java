package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;
    private AnalyticsService analyticsService;
    private UserStatusService userStatusService;
    private UserProfileService userProfileService;
    private UserInfoService userInfoService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        analyticsService = Mockito.mock(AnalyticsService.class);
        userStatusService = Mockito.mock(UserStatusService.class);
        userProfileService = Mockito.mock(UserProfileService.class);
        userInfoService = Mockito.mock(UserInfoService.class);
        userService = new UserService(userRepository, analyticsService, userStatusService, userProfileService, userInfoService);
    }

    @Test
    public void testUpdateUserInfo(){
        User currentUser = new User();
        currentUser.setUsername("test");
        currentUser.getUserStatus().setIsLoggedIn(true);
        currentUser.getUserInfo().setEmail("email@tud.com");

        User modifiedUser = new User();
        modifiedUser.setUsername("test");
        modifiedUser.getUserStatus().setIsLoggedIn(true);
        modifiedUser.getUserInfo().setEmail("email2@tud.com");
        modifiedUser.getUserInfo().setFirstName("bob");
        modifiedUser.getUserInfo().setLastName("bobby");
        modifiedUser.getUserProfile().setBio("nice guy");
        modifiedUser.getUserProfile().setProfilePicture("pic");
        modifiedUser.getUserProfile().setLocation("location");
        modifiedUser.getUserInfo().setPassword("pass");
        modifiedUser.getUserProfile().setFavoriteBook("Crime and Punishment");
        modifiedUser.getUserProfile().setFavoriteGenres(new ArrayList<>());
        Mockito.when(userRepository.findById(modifiedUser.getUsername())).thenReturn(Optional.of(currentUser));
        userService.updateUserInfo(modifiedUser);
        assertEquals(currentUser.getUserInfo().getEmail(),modifiedUser.getUserInfo().getEmail());
        assertEquals(currentUser.getUserInfo().getFirstName(),modifiedUser.getUserInfo().getFirstName());
        assertEquals(currentUser.getUserInfo().getLastName(),modifiedUser.getUserInfo().getLastName());
        assertEquals(currentUser.getUserProfile().getBio(),modifiedUser.getUserProfile().getBio());
        assertEquals(currentUser.getUserProfile().getProfilePicture(),modifiedUser.getUserProfile().getProfilePicture());
        assertEquals(currentUser.getUserProfile().getLocation(),modifiedUser.getUserProfile().getLocation());
        assertEquals(currentUser.getUserInfo().getPassword(),modifiedUser.getUserInfo().getPassword());
        assertEquals(currentUser.getUserProfile().getFavoriteBook(),modifiedUser.getUserProfile().getFavoriteBook());
        assertEquals(currentUser.getUserProfile().getFavoriteGenres(),modifiedUser.getUserProfile().getFavoriteGenres());
        Mockito.verify(userRepository,Mockito.times(1)).saveAndFlush(currentUser);
    }

    @Test
    public void testModifyActivationStatus(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsActive(false);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        userService.modifyUserActivationStatus(user.getUsername(),true);
        assertEquals(user.getUserStatus().getIsActive(),true);
        Mockito.verify(userRepository,Mockito.times(1)).saveAndFlush(user);
    }

    @Test
    public void testFetchUser(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setUserRole(User.UserRoleEnum.REGULAR);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        User fetchedUser = userService.fetchUser(user.getUsername());
        assertEquals(user.getUserStatus().getUserRole(),fetchedUser.getUserStatus().getUserRole());
        Mockito.verify(userRepository,Mockito.times(1)).findById(user.getUsername());
    }

    @Test
    public void testDeleteUser(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getUsername());
        Mockito.verify(userRepository,Mockito.times(1)).deleteById(user.getUsername());
    }

    @Test
    public void testDeleteUserAndFollowing(){
        User user = new User();
        user.setUsername("test");

        User user2 = new User();
        user2.setUsername("123");

        List<String> following = new LinkedList<>();
        following.add("123");
        user.setFollowing(following);

        List<String> followers = new LinkedList<>();
        followers.add("test");
        user2.setFollowers(followers);

        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsById(user2.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user2.getUsername())).thenReturn(Optional.of(user2));

        userService.deleteUser(user.getUsername());

        Mockito.verify(userRepository,Mockito.times(1)).deleteById(user.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findById("123");
        assertEquals(new LinkedList<>(), user2.getFollowers());
    }

    @Test
    public void testDeleteUserAndFollowers(){
        User user = new User();
        user.setUsername("test");

        User user2 = new User();
        user2.setUsername("123");

        List<String> followers = new LinkedList<>();
        followers.add("123");
        user.setFollowers(followers);

        List<String> following = new LinkedList<>();
        following.add("test");
        user2.setFollowing(following);

        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsById(user2.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user2.getUsername())).thenReturn(Optional.of(user2));

        userService.deleteUser(user.getUsername());

        Mockito.verify(userRepository,Mockito.times(1)).deleteById(user.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).findById("123");
        assertEquals(new LinkedList<>(), user2.getFollowing());
    }

    @Test
    public void testLogInUser(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(false);
        Analytics analytics = new Analytics();
        Mockito.when(analyticsService.getAnalytics(user.getUsername())).thenReturn(analytics);
        userService.logInUser(user);
        assertEquals(true,user.getUserStatus().getIsLoggedIn());
        Mockito.verify(analyticsService,Mockito.times(1)).editAnalytics(user.getUsername(),analytics);
    }
    @Test
    public void testLogOutUser(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(true);
        userService.logOutUser(user);
        assertEquals(false,user.getUserStatus().getIsLoggedIn());
        Mockito.verify(userRepository,Mockito.times(2)).saveAndFlush(user);
    }

    @Test
    public void createUser(){
        User user = new User();
        user.setUsername("test");
        Analytics analytics = new Analytics(user.getUsername());
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        User u = userService.createUser(user);
        assertEquals(user, u);
        Mockito.verify(analyticsService,Mockito.times(1)).createAnalytics(analytics);
    }

}
