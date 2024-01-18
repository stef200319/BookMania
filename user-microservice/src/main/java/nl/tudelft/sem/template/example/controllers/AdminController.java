package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.authenticationStrategy.AdminAuthentication;
import nl.tudelft.sem.template.example.authenticationStrategy.Authenticate;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.UserService;
import nl.tudelft.sem.template.example.userHandlers.AdminValidator;
import nl.tudelft.sem.template.example.userHandlers.UserExistingValidator;
import nl.tudelft.sem.template.example.userHandlers.UserLoggedInValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AdminController {
    private final UserRepository userRepo;
    private final UserService userService;
    private final Authenticate authenticator;

    /**
     * Create a user controller.
     *
     * @param userRepo The repository to save the user to.
     * @param userService The service that handles all the logic.
     * @param authenticator Authenticator for admins
     */
    @Autowired
    public AdminController(UserRepository userRepo, UserService userService, AdminAuthentication authenticator) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.authenticator = authenticator;
    }

    /**
     * Changes a user activation (can only be done by an admin).
     *
     * @param adminUsername The username of the admin.
     * @param username The username of the user to change.
     * @param flag What to set the activation to.
     * @return Whether the activation was changed successfully.
     */
    @PutMapping("/setActive/{adminUsername}/{username}")
    public ResponseEntity changeActivation(
        @PathVariable String adminUsername,
        @PathVariable String username,
        @RequestBody boolean flag
    ) {
        UserExistingValidator handler = new UserExistingValidator(userRepo);
        UserLoggedInValidator liv = new UserLoggedInValidator(userRepo);
        liv.setNext(new AdminValidator(userRepo));
        handler.setNext(liv);

        // Authorize the admin
        if (!authenticator.auth(adminUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only an admin can perform this operation");
        }

        User admin = new User();
        admin.setUsername(adminUsername);

        try {
            handler.handle(admin);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            switch (e.getMessage()) {
                case "User does not exist" -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the admin is not valid");
                }
                case "User is not logged in" -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin is not logged in");
                }
                case "User is not an admin" -> {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only an admin can perform this operation");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
                }
            }
        }

        UserExistingValidator handlerUser = new UserExistingValidator(userRepo);
        User u = new User();
        u.setUsername(username);

        try {
            handlerUser.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Modify the activation status of the user
        userService.modifyUserActivationStatus(username, flag);
        return ResponseEntity.status(HttpStatus.OK).body("Activation status changed successfully");
    }

    /**
     * Delete a user. (Can only be done by an admin).
     *
     * @param adminUsername The username of the admin.
     * @param username The username of the user to delete.
     * @return Whether the operation was successful.
     */
    @DeleteMapping("/delete/{adminUsername}/{username}")
    public ResponseEntity deleteUser(@PathVariable String adminUsername, @PathVariable String username) {
        UserExistingValidator handler = new UserExistingValidator(userRepo);
        UserLoggedInValidator liv = new UserLoggedInValidator(userRepo);
        liv.setNext(new AdminValidator(userRepo));
        handler.setNext(liv);

        // Authorize the admin
        if (!authenticator.auth(adminUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only an admin can perform this operation");
        }
        User admin = new User();
        admin.setUsername(adminUsername);

        try {
            handler.handle(admin);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            switch (e.getMessage()) {
                case "User does not exist" -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the admin is not valid");
                }
                case "User is not logged in" -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin is not logged in");
                }
                case "User is not an admin" -> {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only an admin can perform this operation");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
                }
            }
        }

        UserExistingValidator handlerUser = new UserExistingValidator(userRepo);
        User u = new User();
        u.setUsername(username);

        try {
            handlerUser.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Delete the user account
        userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).body("User account deleted successfully");
    }
}
