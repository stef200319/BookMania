package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;

import nl.tudelft.sem.template.example.services.UserService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
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

    @Test
    public void testCreateUserWTakenUsername(){
        User user = new User();
        user.setUsername("username");
        Mockito.when(userRepository.existsById("username")).thenReturn(true);
        ResponseEntity response = userController.createUser(user);
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        assertEquals("Username already in use",response.getBody());
    }
    @Test
    public void testCreateUserWInvalidUsername(){
        User user = new User();
        user.setUsername("12abc/def&");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid username format. The username must contain only alphanumeric characters.");
    }
    @Test
    public void testCreateUserWInvalidEmail1(){
        User user = new User();
        user.setUsername("test");
        user.setEmail("email.com");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid email format");
    }
    @Test
    public void testCreateUserWInvalidEmail2(){
        User user = new User();
        user.setUsername("test");
        user.setEmail("email@tud.toolongsuffix");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid email format");
    }
    @Test
    public void testCreateInvalidUser1(){
        User user = new User();
        user.setUsername("test");
        user.setEmail("email@tud.com");
        user.setIsActive(false);
        user.setIsBanned(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid user object");
    }
    @Test
    public void testCreateInvalidUser2(){
        User user = new User();
        user.setUsername("test");
        user.setEmail("email@tud.com");
        user.setIsActive(true);
        user.setIsBanned(true);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid user object");
    }
    @Test
    public void testCreateValidUser(){
        User user = new User();
        user.setUsername("test");
        user.setEmail("email@tud.com");
        user.setIsActive(true);
        user.setIsBanned(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),user);
        Mockito.verify(userRepository,Mockito.times(1)).saveAndFlush(user);
    }
    @Test
    public void testUpdateNotExistingUserRoute(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.updateUserInfo(user);
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"The user does not exist");
    }
    @Test
    public void testUpdateNotLoggedInUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.setIsLoggedIn(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.updateUserInfo(user);
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"User is not logged in");
    }
    @Test
    public void testUpdateUserWInvalidEmailRoute(){
        User currentUser = new User();
        currentUser.setUsername("test");
        currentUser.setIsLoggedIn(true);
        currentUser.setEmail("email@tud.com");

        User modifiedUser = new User();
        modifiedUser.setUsername("test");
        modifiedUser.setIsLoggedIn(true);
        modifiedUser.setEmail("email.com");

        Mockito.when(userRepository.existsById(modifiedUser.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(modifiedUser.getUsername())).thenReturn(Optional.of(currentUser));
        ResponseEntity response = userController.updateUserInfo(modifiedUser);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid email format");
    }
    @Test
    public void testUpdateUserRoute(){
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

        Mockito.when(userRepository.existsById(modifiedUser.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(modifiedUser.getUsername())).thenReturn(Optional.of(currentUser));
        ResponseEntity response = userController.updateUserInfo(modifiedUser);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Account updated successfully");
        assertEquals(currentUser.getBio(),modifiedUser.getBio());
        assertEquals(currentUser.getEmail(),modifiedUser.getEmail());
        assertEquals(currentUser.getFirstName(),modifiedUser.getFirstName());
        assertEquals(currentUser.getLastName(), modifiedUser.getLastName());
    }
    @Test
    public void testSelfDeleteNotExistingUserRoute(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.deleteSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"Username is not valid");
    }
    @Test
    public void testSelfDeleteUserNotLoggedInRoute(){
        User user = new User();
        user.setUsername("test");
        user.setIsLoggedIn(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deleteSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"User is not logged in");
        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getUsername());
    }
    @Test
    public void testSelfDeleteUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.setIsLoggedIn(true);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deleteSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Account deleted successfully");
        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getUsername());


    }

    @Test
    public void testGetNotExistingUserRoute(){
        Mockito.when(userRepository.existsById("test")).thenReturn(false);
        ResponseEntity response = userController.getUserByUsername("test");
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"User not found");
    }
    @Test
    public void testGetUserRoute(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.getOne(user.getUsername())).thenReturn(user);
        ResponseEntity response = userController.getUserByUsername(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),user);
    }
    @Test
    public void testSelfDeactivateNotExistentRoute(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.deactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"Username is not valid");
    }
    @Test
    public void testSelfDeactivateNotLoggedInUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.setIsLoggedIn(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"User is not logged in");
    }
    @Test
    public void testSelfDeactivateNotActiveUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.setIsLoggedIn(true);
        user.setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"User account is already inactive");
    }

    @Test
    public void testSelfDeactivateUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.setIsLoggedIn(true);
        user.setIsActive(true);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deactivateSelf(user.getUsername());
        assertEquals(user.getIsActive(),false);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"User account deactivated successfully");
        Mockito.verify(userRepository,Mockito.times(1)).saveAndFlush(user);
    }

    @Test
    public void testSelfActivateNotExistingRoute(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.reactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"Username is not valid");
    }
    @Test
    public void testSelfActivateNotLoggedInUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.setIsLoggedIn(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.reactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"User is not logged in");
    }
    @Test
    public void testSelfActivateActiveUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.setIsLoggedIn(true);
        user.setIsActive(true);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.reactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"User account is already active");
    }

    @Test
    public void testSelfActivateUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.setIsLoggedIn(true);
        user.setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.reactivateSelf(user.getUsername());
        assertEquals(user.getIsActive(),true);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"User account reactivated successfully");
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(user);
    }
    @Test
    public void testAdminActivateRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(true);
        admin.setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        User user = new User();
        user.setUsername("user");
        user.setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(user.getIsActive(),true);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Activation status changed successfully");
        Mockito.verify(userRepository,Mockito.times(1)).saveAndFlush(user);
    }
    @Test
    public void testNotLoggedInAdminActivateRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(false);
        admin.setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        User user = new User();
        user.setUsername("user");
        user.setIsActive(false);
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"Admin is not logged in");
    }
    @Test
    public void testNotValidAdminActivateRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(true);
        admin.setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(false);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        User user = new User();
        user.setUsername("user");
        user.setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Username of the admin is not valid");
    }
    @Test
    public void testNotAdminActivateRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(true);
        admin.setUserRole(User.UserRoleEnum.REGULAR);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        User user = new User();
        user.setUsername("user");
        user.setIsActive(false);
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.FORBIDDEN);
        assertEquals(response.getBody(),"Only an admin can perform this operation");
    }
    @Test
    public void testAdminActivateNotValidUserRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(true);
        admin.setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        User user = new User();
        user.setUsername("user");
        user.setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"User not found");
    }
    @Test
    public void testAdminDeleteRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(true);
        admin.setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        User user = new User();
        user.setUsername("user");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deleteUser(admin.getUsername(),user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"User account deleted successfully");
        Mockito.verify(userRepository,Mockito.times(1)).deleteById(user.getUsername());
    }
    @Test
    public void testNotLoggedInAdminDeleteRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(false);
        admin.setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        User user = new User();
        user.setUsername("user");
        ResponseEntity response = userController.deleteUser(admin.getUsername(),user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"Admin is not logged in");
    }
    @Test
    public void testNotValidAdminDeleteRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(true);
        admin.setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(false);
        User user = new User();
        user.setUsername("user");
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Username of the admin is not valid");
    }
    @Test
    public void testNotAdminDeleteRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(true);
        admin.setUserRole(User.UserRoleEnum.REGULAR);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        User user = new User();
        user.setUsername("user");
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.FORBIDDEN);
        assertEquals(response.getBody(),"Only an admin can perform this operation");
    }
    @Test
    public void testAdminDeleteNotValidUserRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setIsLoggedIn(true);
        admin.setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        User user = new User();
        user.setUsername("user");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.deleteUser(admin.getUsername(),user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"User not found");
    }


}
