package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.LoginRequest;
import nl.tudelft.sem.template.example.model.User;

import nl.tudelft.sem.template.example.services.SearchService;
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
    private SearchService searchService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = Mockito.mock(UserService.class);
        searchService = Mockito.mock(SearchService.class);
        userController = new UserController(userRepository, userService, searchService);
    }
    @Test
    public void testInvalidLogInWithInvalidUsername() {
        LoginRequest validUser = new LoginRequest("testUsername", "testPassword");
//        validUser.setUsername("testUsername");
//        validUser.getUserInfo().setPassword("testPassword");
//        validUser.getUserStatus().setIsActive(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(false);

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
    }

    @Test
    public void testInvalidLogInWithInvalidIsActive() {
        User validUser1 = new User();
        validUser1.setUsername("testUsername");
        validUser1.getUserInfo().setPassword("testPassword");
        validUser1.getUserStatus().setIsActive(false);

        LoginRequest validUser = new LoginRequest("testUsername", "testPassword");
//        validUser.setUsername("testUsername");
//        validUser.getUserInfo().setPassword("testPassword");
//        validUser.getUserStatus().setIsActive(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser1));

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User account is not active", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testInvalidLogInWithInvalidPassword() {
        User validUser1 = new User();
        validUser1.setUsername("testUsername");
        validUser1.getUserInfo().setPassword("testPassword");
        validUser1.getUserStatus().setIsActive(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser1));

        User user2 = new User();
        LoginRequest validUser = new LoginRequest("testUsername", "wrongPassword");
        user2.setUsername("testUsername");
        user2.getUserInfo().setPassword("wrongPassword");
        user2.getUserStatus().setIsActive(true);

        ResponseEntity response = userController.logInUser(validUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid password supplied", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testAlreadyLoggedIn() {

        User validUser = new User();
        LoginRequest validUser1 = new LoginRequest("testUsername", "testPassword");
        validUser.setUsername("testUsername");
        validUser.getUserInfo().setPassword("testPassword");
        validUser.getUserStatus().setIsActive(true);
        validUser.getUserStatus().setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logInUser(validUser1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User already logged in", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findById("testUsername");
    }

    @Test
    public void testValidLogIn() {
        LoginRequest validUser1 = new LoginRequest("testUsername", "testPassword");
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.getUserInfo().setPassword("testPassword");
        validUser.getUserStatus().setIsActive(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logInUser(validUser1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged in successfully", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(2)).findById("testUsername");
    }

    @Test
    public void testInvalidLogOutWithInvalidUsername() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.getUserInfo().setPassword("testPassword");
        validUser.getUserStatus().setIsActive(true);
        validUser.getUserStatus().setIsLoggedIn(true);

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
        validUser.getUserInfo().setPassword("testPassword");
        validUser.getUserStatus().setIsActive(true);
        validUser.getUserStatus().setIsLoggedIn(null);

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
        validUser.getUserInfo().setPassword("testPassword");
        validUser.getUserStatus().setIsActive(true);
        validUser.getUserStatus().setIsLoggedIn(false);

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
        validUser.getUserInfo().setPassword("testPassword");
        validUser.getUserStatus().setIsActive(true);
        validUser.getUserStatus().setIsLoggedIn(true);

        Mockito.when(userRepository.existsById("testUsername")).thenReturn(true);
        Mockito.when(userRepository.findById("testUsername")).thenReturn(Optional.of(validUser));

        ResponseEntity response = userController.logOutUser("testUsername");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged out successfully", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById("testUsername");
        Mockito.verify(userRepository, Mockito.times(2)).findById("testUsername");
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
        user.getUserInfo().setEmail("email.com");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid email format");
    }
    @Test
    public void testCreateUserWInvalidEmail2(){
        User user = new User();
        user.setUsername("test");
        user.getUserInfo().setEmail("email@tud.toolongsuffix");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid email format");
    }
    @Test
    public void testCreateInvalidUser1(){
        User user = new User();
        user.setUsername("test");
        user.getUserInfo().setEmail("email@tud.com");
        user.getUserStatus().setIsActive(false);
        user.getUserStatus().setIsBanned(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid user object");
    }
    @Test
    public void testCreateInvalidUser2(){
        User user = new User();
        user.setUsername("test");
        user.getUserInfo().setEmail("email@tud.com");
        user.getUserStatus().setIsActive(true);
        user.getUserStatus().setIsBanned(true);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.createUser(user);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Invalid user object");
    }
    @Test
    public void testCreateInvalidInfoUser(){
        User user = new User();
        user.setUsername("test");
        user.getUserInfo().setUsername("testt");
        user.getUserStatus().setUsername("test");
        user.getUserProfile().setUsername("test");
        user.getUserInfo().setEmail("email@tud.com");
        user.getUserStatus().setIsActive(true);
        user.getUserStatus().setIsBanned(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        Mockito.when(userService.createUser(user)).thenReturn(user);
        ResponseEntity response = userController.createUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The usernames of the user do not match with the ones of the info.", response.getBody());
    }
    @Test
    public void testCreateStatusInfoUser(){
        User user = new User();
        user.setUsername("test");
        user.getUserInfo().setUsername("test");
        user.getUserStatus().setUsername("testt");
        user.getUserProfile().setUsername("test");
        user.getUserInfo().setEmail("email@tud.com");
        user.getUserStatus().setIsActive(true);
        user.getUserStatus().setIsBanned(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        Mockito.when(userService.createUser(user)).thenReturn(user);
        ResponseEntity response = userController.createUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The usernames of the user do not match with the ones of the status.", response.getBody());
    }
    @Test
    public void testCreateInvalidProfileUser(){
        User user = new User();
        user.setUsername("test");
        user.getUserInfo().setUsername("test");
        user.getUserStatus().setUsername("test");
        user.getUserProfile().setUsername("testt");
        user.getUserInfo().setEmail("email@tud.com");
        user.getUserStatus().setIsActive(true);
        user.getUserStatus().setIsBanned(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        Mockito.when(userService.createUser(user)).thenReturn(user);
        ResponseEntity response = userController.createUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The usernames of the user do not match with the ones of the profile.", response.getBody());
    }
    @Test
    public void testCreateValidUser(){
        User user = new User();
        user.setUsername("test");
        user.getUserInfo().setUsername("test");
        user.getUserStatus().setUsername("test");
        user.getUserProfile().setUsername("test");
        user.getUserInfo().setEmail("email@tud.com");
        user.getUserStatus().setIsActive(true);
        user.getUserStatus().setIsBanned(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        Mockito.when(userService.createUser(user)).thenReturn(user);
        ResponseEntity response = userController.createUser(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(),user);
        Mockito.verify(userService,Mockito.times(1)).createUser(user);
    }
    @Test
    public void testUpdateNotExistingUserRoute(){
        User user = new User();
        user.getUserInfo().setEmail("asd@fds.com");
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
        user.getUserInfo().setUsername("test");
        user.getUserStatus().setUsername("test");
        user.getUserProfile().setUsername("test");
        user.getUserInfo().setEmail("asd@fds.com");
        user.getUserStatus().setIsLoggedIn(false);
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
        currentUser.getUserStatus().setIsLoggedIn(true);
        currentUser.getUserInfo().setEmail("email@tud.com");

        User modifiedUser = new User();
        modifiedUser.setUsername("test");
        modifiedUser.getUserStatus().setIsLoggedIn(true);
        modifiedUser.getUserInfo().setEmail("email.com");

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
        currentUser.getUserStatus().setIsLoggedIn(true);
        currentUser.getUserInfo().setEmail("email@tud.com");
        currentUser.getUserInfo().setUsername("test");
        currentUser.getUserStatus().setUsername("test");
        currentUser.getUserProfile().setUsername("test");

        User modifiedUser = new User();
        modifiedUser.setUsername("test");
        modifiedUser.getUserStatus().setIsLoggedIn(true);
        modifiedUser.getUserInfo().setEmail("email2@tud.com");
        modifiedUser.getUserInfo().setFirstName("bob");
        modifiedUser.getUserInfo().setLastName("bobby");
        //modifiedUser.setBio("nice guy");

        Mockito.when(userRepository.existsById(modifiedUser.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(modifiedUser.getUsername())).thenReturn(Optional.of(currentUser));
        ResponseEntity response = userController.updateUserInfo(modifiedUser);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Account updated successfully");
        Mockito.verify(userService,Mockito.times(1)).updateUserInfo(modifiedUser);
    }
    @Test
    public void testUpdateUserInvalidInfo(){
        User currentUser = new User();
        currentUser.setUsername("test");
        currentUser.getUserStatus().setIsLoggedIn(true);
        currentUser.getUserInfo().setEmail("email@tud.com");
        currentUser.getUserInfo().setUsername("testt");
        currentUser.getUserStatus().setUsername("test");
        currentUser.getUserProfile().setUsername("test");

        User modifiedUser = new User();
        modifiedUser.setUsername("test");
        modifiedUser.getUserStatus().setIsLoggedIn(true);
        modifiedUser.getUserInfo().setEmail("email2@tud.com");
        modifiedUser.getUserInfo().setFirstName("bob");
        modifiedUser.getUserInfo().setLastName("bobby");
        //modifiedUser.setBio("nice guy");

        Mockito.when(userRepository.existsById(modifiedUser.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(modifiedUser.getUsername())).thenReturn(Optional.of(currentUser));
        ResponseEntity response = userController.updateUserInfo(modifiedUser);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"The usernames of the user do not match with the ones of the info.");
    }
    @Test
    public void testUpdateUserInvalidStatus(){
        User currentUser = new User();
        currentUser.setUsername("test");
        currentUser.getUserStatus().setIsLoggedIn(true);
        currentUser.getUserInfo().setEmail("email@tud.com");
        currentUser.getUserInfo().setUsername("test");
        currentUser.getUserStatus().setUsername("testt");
        currentUser.getUserProfile().setUsername("test");

        User modifiedUser = new User();
        modifiedUser.setUsername("test");
        modifiedUser.getUserStatus().setIsLoggedIn(true);
        modifiedUser.getUserInfo().setEmail("email2@tud.com");
        modifiedUser.getUserInfo().setFirstName("bob");
        modifiedUser.getUserInfo().setLastName("bobby");
        //modifiedUser.setBio("nice guy");

        Mockito.when(userRepository.existsById(modifiedUser.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(modifiedUser.getUsername())).thenReturn(Optional.of(currentUser));
        ResponseEntity response = userController.updateUserInfo(modifiedUser);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"The usernames of the user do not match with the ones of the status.");
    }
    @Test
    public void testUpdateUserInvalidProfile(){
        User currentUser = new User();
        currentUser.setUsername("test");
        currentUser.getUserStatus().setIsLoggedIn(true);
        currentUser.getUserInfo().setEmail("email@tud.com");
        currentUser.getUserInfo().setUsername("test");
        currentUser.getUserStatus().setUsername("test");
        currentUser.getUserProfile().setUsername("testt");

        User modifiedUser = new User();
        modifiedUser.setUsername("test");
        modifiedUser.getUserStatus().setIsLoggedIn(true);
        modifiedUser.getUserInfo().setEmail("email2@tud.com");
        modifiedUser.getUserInfo().setFirstName("bob");
        modifiedUser.getUserInfo().setLastName("bobby");
        //modifiedUser.setBio("nice guy");

        Mockito.when(userRepository.existsById(modifiedUser.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(modifiedUser.getUsername())).thenReturn(Optional.of(currentUser));
        ResponseEntity response = userController.updateUserInfo(modifiedUser);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"The usernames of the user do not match with the ones of the profile.");
    }
    @Test
    public void testSelfDeleteNotExistingUserRoute(){
        User user = new User();
        user.setUsername("test");
        ResponseEntity response = userController.deleteSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"Username is not valid");
        Mockito.verify(userService,Mockito.times(0)).deleteUser(user.getUsername());
    }
    @Test
    public void testSelfDeleteUserNotLoggedInRoute(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deleteSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"User is not logged in");
        Mockito.verify(userService, Mockito.times(0)).deleteUser(user.getUsername());
    }
    @Test
    public void testSelfDeleteUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(true);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deleteSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"Account deleted successfully");
        Mockito.verify(userService, Mockito.times(1)).deleteUser(user.getUsername());
    }

    @Test
    public void testGetNotExistingUserRoute(){
        Mockito.when(userRepository.existsById("test")).thenReturn(false);
        ResponseEntity response = userController.getUserByUsername("test");
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"User not found");
        Mockito.verify(userService,Mockito.times(0)).fetchUser("test");
    }
    @Test
    public void testGetUserRoute(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userService.fetchUser(user.getUsername())).thenReturn(user);
        ResponseEntity response = userController.getUserByUsername(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),user);
        Mockito.verify(userService,Mockito.times(1)).fetchUser(user.getUsername());
    }
    @Test
    public void testSelfDeactivateNotExistentRoute(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.deactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"Username is not valid");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }
    @Test
    public void testSelfDeactivateNotLoggedInUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"User is not logged in");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }
    @Test
    public void testSelfDeactivateNotActiveUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(true);
        user.getUserStatus().setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"User account is already inactive");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }

    @Test
    public void testSelfDeactivateUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(true);
        user.getUserStatus().setIsActive(true);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"User account deactivated successfully");
        Mockito.verify(userService,Mockito.times(1)).modifyUserActivationStatus(user.getUsername(),false);
    }

    @Test
    public void testSelfActivateNotExistingRoute(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.reactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"Username is not valid");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }
    @Test
    public void testSelfActivateNotLoggedInUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.reactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"User is not logged in");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }
    @Test
    public void testSelfActivateActiveUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(true);
        user.getUserStatus().setIsActive(true);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.reactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"User account is already active");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }

    @Test
    public void testSelfActivateUserRoute(){
        User user = new User();
        user.setUsername("test");
        user.getUserStatus().setIsLoggedIn(true);
        user.getUserStatus().setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.reactivateSelf(user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"User account reactivated successfully");
        Mockito.verify(userService,Mockito.times(1)).modifyUserActivationStatus(user.getUsername(),true);
    }


    @Test
    public void testSearchUserNull() {
        String query = "q";
        String searchBy = null;
        Boolean isAuthor = null;

        User u = new User();
        List<User> ans = new LinkedList<>();
        ans.add(u);

        Mockito.when(searchService.findUsersByName(query, true)).thenReturn(ans);

        ResponseEntity response = userController.searchUser(query, searchBy, isAuthor);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),ans);
    }

    @Test
    public void testSearchUserBad() {
        String query = "q";
        String searchBy = "abc";
        Boolean isAuthor = null;

        User u = new User();
        List<User> ans = new LinkedList<>();
        ans.add(u);

        ResponseEntity response = userController.searchUser(query, searchBy, isAuthor);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSearchByName() {
        String query = "q";
        String searchBy = "name";
        Boolean isAuthor = true;

        User u = new User();
        List<User> ans = new LinkedList<>();
        ans.add(u);

        Mockito.when(searchService.findUsersByName(query, true)).thenReturn(ans);

        ResponseEntity response = userController.searchUser(query, searchBy, isAuthor);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),ans);
        Mockito.verify(searchService,Mockito.times(1)).findUsersByName(query, true);
    }

    @Test
    public void testSearchByGenre() {
        String query = "q";
        String searchBy = "genre";
        Boolean isAuthor = true;

        User u = new User();
        List<User> ans = new LinkedList<>();
        ans.add(u);

        Mockito.when(searchService.findUsersByGenre(query, true)).thenReturn(ans);

        ResponseEntity response = userController.searchUser(query, searchBy, isAuthor);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),ans);
        Mockito.verify(searchService,Mockito.times(1)).findUsersByGenre(query, true);
    }

    @Test
    public void testSearchByBook() {
        String query = "q";
        String searchBy = "favorite_book";
        Boolean isAuthor = true;

        User u = new User();
        List<User> ans = new LinkedList<>();
        ans.add(u);

        Mockito.when(searchService.findUsersByFavoriteBook(query, true)).thenReturn(ans);

        ResponseEntity response = userController.searchUser(query, searchBy, isAuthor);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),ans);
        Mockito.verify(searchService,Mockito.times(1)).findUsersByFavoriteBook(query, true);
    }

    @Test
    public void testSearchByFollows() {
        String query = "q";
        String searchBy = "follows";
        Boolean isAuthor = true;

        User u = new User();
        List<User> ans = new LinkedList<>();
        ans.add(u);

        Mockito.when(searchService.findUsersByFollows(query, true)).thenReturn(ans);

        ResponseEntity response = userController.searchUser(query, searchBy, isAuthor);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),ans);
        Mockito.verify(searchService,Mockito.times(1)).findUsersByFollows(query, true);
    }


}
