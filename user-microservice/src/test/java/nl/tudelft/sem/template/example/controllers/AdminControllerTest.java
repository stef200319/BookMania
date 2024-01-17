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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminControllerTest {
    private UserRepository userRepository;
    private UserService userService;
    private AdminController userController;
    private AdminAuthentication authenticator;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = Mockito.mock(UserService.class);
        authenticator = Mockito.mock(AdminAuthentication.class);
        userController = new AdminController(userRepository, userService, authenticator);
    }

    @Test
    public void testAdminActivateRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(true);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(true);
        User user = new User();
        user.setUsername("user");
        user.getUserStatus().setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity
            response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(),"Activation status changed successfully");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }
    @Test
    public void testNotLoggedInAdminActivateRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(false);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(true);
        User user = new User();
        user.setUsername("user");
        user.getUserStatus().setIsActive(false);
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"Admin is not logged in");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }
    @Test
    public void testNotValidAdminActivateRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(true);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(false);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(true);
        User user = new User();
        user.setUsername("user");
        user.getUserStatus().setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Username of the admin is not valid");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }
    @Test
    public void testNotAdminActivateRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(true);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.REGULAR);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(false);
        User user = new User();
        user.setUsername("user");
        user.getUserStatus().setIsActive(false);
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.FORBIDDEN);
        assertEquals(response.getBody(),"Only an admin can perform this operation");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }
    @Test
    public void testAdminActivateNotValidUserRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(true);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(true);
        User user = new User();
        user.setUsername("user");
        user.getUserStatus().setIsActive(false);
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.changeActivation(admin.getUsername(),user.getUsername(),true);
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"User not found");
        Mockito.verify(userService,Mockito.times(0)).modifyUserActivationStatus(user.getUsername(),false);
    }
    @Test
    public void testAdminDeleteRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(true);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(true);
        User user = new User();
        user.setUsername("user");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        ResponseEntity response = userController.deleteUser(admin.getUsername(),user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertEquals(response.getBody(),"User account deleted successfully");
        Mockito.verify(userService,Mockito.times(1)).deleteUser(user.getUsername());
    }
    @Test
    public void testNotLoggedInAdminDeleteRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(false);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(true);
        User user = new User();
        user.setUsername("user");
        ResponseEntity response = userController.deleteUser(admin.getUsername(),user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.UNAUTHORIZED);
        assertEquals(response.getBody(),"Admin is not logged in");
        Mockito.verify(userService,Mockito.times(0)).deleteUser(user.getUsername());
    }
    @Test
    public void testNotValidAdminDeleteRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(true);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(false);
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(true);
        User user = new User();
        user.setUsername("user");
        ResponseEntity response = userController.deleteUser(admin.getUsername(),user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
        assertEquals(response.getBody(),"Username of the admin is not valid");
        Mockito.verify(userService,Mockito.times(0)).deleteUser(user.getUsername());
    }
    @Test
    public void testNotAdminDeleteRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(true);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.REGULAR);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(false);
        User user = new User();
        user.setUsername("user");
        ResponseEntity response = userController.deleteUser(admin.getUsername(),user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.FORBIDDEN);
        assertEquals(response.getBody(),"Only an admin can perform this operation");
        Mockito.verify(userService,Mockito.times(0)).deleteUser(user.getUsername());
    }
    @Test
    public void testAdminDeleteNotValidUserRoute(){
        User admin = new User();
        admin.setUsername("admin");
        admin.getUserStatus().setIsLoggedIn(true);
        admin.getUserStatus().setUserRole(User.UserRoleEnum.ADMIN);
        Mockito.when(userRepository.existsById(admin.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(admin.getUsername())).thenReturn(Optional.of(admin));
        Mockito.when(authenticator.auth(admin.getUsername())).thenReturn(true);
        User user = new User();
        user.setUsername("user");
        Mockito.when(userRepository.existsById(user.getUsername())).thenReturn(false);
        ResponseEntity response = userController.deleteUser(admin.getUsername(),user.getUsername());
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
        assertEquals(response.getBody(),"User not found");
        Mockito.verify(userService,Mockito.times(0)).deleteUser(user.getUsername());
    }
}
