package nl.tudelft.sem.template.example.controllers;

import java.util.List;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.UserService;
import nl.tudelft.sem.template.example.userHandlers.AdminValidator;
import nl.tudelft.sem.template.example.userHandlers.EmailValidator;
import nl.tudelft.sem.template.example.userHandlers.PasswordValidator;
import nl.tudelft.sem.template.example.userHandlers.UserActiveValidator;
import nl.tudelft.sem.template.example.userHandlers.UserAlreadyActiveValidator;
import nl.tudelft.sem.template.example.userHandlers.UserAlreadyLoggedInValidator;
import nl.tudelft.sem.template.example.userHandlers.UserBannedValidator;
import nl.tudelft.sem.template.example.userHandlers.UserDifferentInfoUsernameValidator;
import nl.tudelft.sem.template.example.userHandlers.UserDifferentProfileUsernameValidator;
import nl.tudelft.sem.template.example.userHandlers.UserDifferentStatusUsernameValidator;
import nl.tudelft.sem.template.example.userHandlers.UserExistingValidator;
import nl.tudelft.sem.template.example.userHandlers.UserLoggedInValidator;
import nl.tudelft.sem.template.example.userHandlers.UsernameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepo;
    private final UserService userService;

    /**
     * Create a user controller.
     *
     * @param userRepo The repository to save the user to.
     * @param userService The service that handles all the logic.
     */
    @Autowired
    public UserController(UserRepository userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    /**
     * Log in the user.
     *
     * @param loginRequest Username and password of the user.
     * @return Whether the user was successfuly logged in.
     */
    @PostMapping("/login")
    public ResponseEntity logInUser(@RequestBody User loginRequest) {
        UserExistingValidator handler = new UserExistingValidator(userRepo);
        PasswordValidator pv = new PasswordValidator(userRepo, loginRequest);
        UserActiveValidator av = new UserActiveValidator(userRepo);
        UserAlreadyLoggedInValidator ali = new UserAlreadyLoggedInValidator(userRepo);

        av.setNext(ali);
        pv.setNext(av);
        handler.setNext(pv);

        try {
            handler.handle(loginRequest);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            switch (e.getMessage()) {
                case "User does not exist" -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }
                case "Wrong password" -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password supplied");
                }
                case "User is inactive" -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User account is not active");
                }
                case "User is logged in" -> {
                    return ResponseEntity.status(HttpStatus.OK).body("User already logged in");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
                }
            }
        }

        User currentUser = this.userRepo.findById(loginRequest.getUsername()).get();
        userService.logInUser(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully");
    }

    /**
     * Log a user out.
     *
     * @param username The username to log out.
     * @return Whether the user was logged out successfully.
     */
    @PostMapping("/logout/{username}")
    public ResponseEntity logOutUser(@PathVariable String username) {
        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);
        handler.setNext(new UserLoggedInValidator(userRepo));

        try {
            handler.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            switch (e.getMessage()) {
                case "User does not exist" -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }
                case "User is not logged in" -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
                }
            }
        }

        // Set the user as logged out
        User currentUser = this.userRepo.findById(username).get();
        userService.logOutUser(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body("User logged out successfully");
    }


    /**
     * Creates a user.
     *
     * @param newUser The user values to save.
     * @return The created user.
     */
    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody User newUser) {
        UserBannedValidator bv = new UserBannedValidator(userRepo);
        UserDifferentInfoUsernameValidator difu = new UserDifferentInfoUsernameValidator(userRepo);
        UserDifferentProfileUsernameValidator difp = new UserDifferentProfileUsernameValidator(userRepo);
        difp.setNext(new UserDifferentStatusUsernameValidator(userRepo));
        difu.setNext(difp);
        bv.setNext(difu);
        UserActiveValidator av = new UserActiveValidator(userRepo);
        av.setNext(bv);
        EmailValidator ev = new EmailValidator(userRepo);
        ev.setNext(av);
        UsernameValidator handler = new UsernameValidator(userRepo);
        handler.setNext(ev);

        try {
            handler.handle(newUser);
        } catch (InvalidUsernameException e) {
            if (e.getMessage().equals("Username already in use")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already in use");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid username format. The username must contain only alphanumeric characters.");
            }
        } catch (InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        } catch (InvalidUserException e) {
            switch (e.getMessage()) {
                case "The usernames of the user do not match with the ones of the info." -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The usernames of the user do not match with the ones of the info.");
                }
                case "The usernames of the user do not match with the ones of the profile." -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The usernames of the user do not match with the ones of the profile.");
                }
                case "The usernames of the user do not match with the ones of the status." -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The usernames of the user do not match with the ones of the status.");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user object");
                }
            }
        }

        User saved = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(saved);
    }

    /**
     * Update a user entity.
     *
     * @param modifiedUser Changes to the user entity.
     * @return The new user values.
     */
    @PutMapping
    public ResponseEntity updateUserInfo(@RequestBody User modifiedUser) {
        UserLoggedInValidator lv = new UserLoggedInValidator(userRepo);
        UserDifferentInfoUsernameValidator difu = new UserDifferentInfoUsernameValidator(userRepo);
        UserDifferentProfileUsernameValidator difp = new UserDifferentProfileUsernameValidator(userRepo);
        difp.setNext(new UserDifferentStatusUsernameValidator(userRepo));
        difu.setNext(difp);
        lv.setNext(difu);
        UserExistingValidator ev =  new UserExistingValidator(userRepo);
        ev.setNext(lv);
        EmailValidator handler = new EmailValidator(userRepo);
        handler.setNext(ev);

        try {
            handler.handle(modifiedUser);
        } catch (InvalidUsernameException | InvalidUserException e) {
            switch (e.getMessage()) {
                case "User does not exist" -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The user does not exist");
                }
                case "The usernames of the user do not match with the ones of the info." -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The usernames of the user do not match with the ones of the info.");
                }
                case "The usernames of the user do not match with the ones of the profile." -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The usernames of the user do not match with the ones of the profile.");
                }
                case "The usernames of the user do not match with the ones of the status." -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The usernames of the user do not match with the ones of the status.");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User is not logged in");
                }
            }
        } catch (InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }
        userService.updateUserInfo(modifiedUser);
        return ResponseEntity.status(HttpStatus.OK).body("Account updated successfully");
    }

    /**
     * Used when a user wants to delete themselves.
     *
     * @param username The username to delete.
     * @return Whether the user was deleted successfully.
     */
    @DeleteMapping("/delete/{username}")
    public ResponseEntity deleteSelf(@PathVariable String username) {

        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);    // Check if the provided username is valid
        handler.setNext(new UserLoggedInValidator(userRepo));                   // Check whether the User is logged in

        try {
            handler.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is not valid");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
            }
        }

        // Remove the User entity from the DB
        userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).body("Account deleted successfully");

    }

    /**
     * Get a user with a given username.
     *
     * @param username The username.
     * @return The user if they exist.
     */
    @GetMapping("/{username}")
    public ResponseEntity getUserByUsername(@PathVariable String username) {
        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);

        try {
            handler.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User fetchedUser = userService.fetchUser(username);
        return ResponseEntity.status(HttpStatus.OK).body(fetchedUser);
    }


    /**
     * Search the user repository.
     *
     * @param query The search query.
     * @param searchBy What to search by. Name or Genre or Favorite Book or Follows
     * @param isAuthor Whether the user is an author
     * @return The search results.
     */
    @GetMapping("/search")
    ResponseEntity<List<User>> searchUser(
        String query,
        String searchBy,
        Boolean isAuthor
    ) {
        if (searchBy == null) {
            searchBy = "name";
        }
        if (isAuthor == null) {
            isAuthor = true;
        }

        List<User> foundUsers;
        switch (searchBy) {
            case "name" -> {
                foundUsers = userService.findUsersByName(query, isAuthor);
            }
            case "genre" -> {
                foundUsers = userService.findUsersByGenre(query, isAuthor);
            }
            case "favorite_book" -> {
                foundUsers = userService.findUsersByFavoriteBook(query, isAuthor);
            }
            case "follows" -> {
                foundUsers = userService.findUsersByFollows(query, isAuthor);
            }

            default -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(foundUsers);
    }

    /**
     * Used when a user wants to deactivate themselves.
     *
     * @param username The username of the user.
     * @return Whether the operation was successful.
     */
    @PutMapping("/deactivate/{username}")
    public ResponseEntity deactivateSelf(@PathVariable String username) {

        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);
        UserLoggedInValidator liv = new UserLoggedInValidator(userRepo);
        liv.setNext(new UserActiveValidator(userRepo));
        handler.setNext(liv);

        try {
            handler.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            switch (e.getMessage()) {
                case "User does not exist" -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is not valid");
                }
                case "User is not logged in" -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
                }
                case "User is inactive" -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User account is already inactive");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
                }
            }
        }

        userService.modifyUserActivationStatus(username, false);
        return ResponseEntity.status(HttpStatus.OK).body("User account deactivated successfully");
    }

    /**
     * Used when the user wants to reactivate themselves.
     *
     * @param username The username of the user.
     * @return Whether the operation was successful.
     */
    @PutMapping("/reactivate/{username}")
    public ResponseEntity reactivateSelf(@PathVariable String username) {

        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);
        UserLoggedInValidator liv = new UserLoggedInValidator(userRepo);
        liv.setNext(new UserAlreadyActiveValidator(userRepo));
        handler.setNext(liv);

        try {
            handler.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            switch (e.getMessage()) {
                case "User does not exist" -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is not valid");
                }
                case "User is not logged in" -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
                }
                case "User is active" -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User account is already active");
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
                }
            }
        }

        userService.modifyUserActivationStatus(username, true);
        return ResponseEntity.status(HttpStatus.OK).body("User account reactivated successfully");
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

        User admin = new User();
        admin.setUsername(adminUsername);
        User u = new User();
        u.setUsername(username);

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

        User admin = new User();
        admin.setUsername(adminUsername);
        User u = new User();
        u.setUsername(username);

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

        try {
            handlerUser.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Delete the user account
        userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).body("User account deleted successfully");
    }

    /**
     * Have user1 follow user2.
     *
     * @param username1 The username of user1.
     * @param username2 The username of user2.
     * @return Whether the operation was successful.
     */
    @PostMapping("/follow/{username1}/{username2}")
    public ResponseEntity followUser(@PathVariable String username1, @PathVariable String username2) {
        User u1 = new User();
        u1.setUsername(username1);

        User u2 = new User();
        u2.setUsername(username2);

        UserExistingValidator handler = new UserExistingValidator(userRepo);

        try {
            handler.handle(u2);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user being followed is not valid");
        }

        handler.setNext(new UserLoggedInValidator(userRepo));

        try {
            handler.handle(u1);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username of the user executing the action is not valid");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User is not logged in");
            }
        }

        User user1 = userRepo.findById(username1).get();
        User user2 = userRepo.findById(username2).get();

        userService.followUser(user1, user2);

        return ResponseEntity.status(HttpStatus.OK).body("User account followed successfully");
    }

    /**
     * Remove user2 from user1's followers.
     *
     * @param username1 Username of user1.
     * @param username2 Username of user2.
     * @return Whether the operation was successful.
     */
    @DeleteMapping("/follow/{username1}/{username2}")
    public ResponseEntity unfollowUser(@PathVariable String username1, @PathVariable String username2) {

        User u1 = new User();
        u1.setUsername(username1);

        User u2 = new User();
        u2.setUsername(username2);

        UserExistingValidator handler1 = new UserExistingValidator(userRepo);
        handler1.setNext(new UserLoggedInValidator(userRepo));
        UserExistingValidator handler2 = new UserExistingValidator(userRepo);

        try {
            handler1.handle(u1);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if (e.getMessage().equals("User does not exist")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username of the user executing the action is not valid");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User is not logged in");
            }
        }

        try {
            handler2.handle(u2);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user being unfollowed is not valid");
        }

        User user1 = userRepo.findById(username1).get();
        User user2 = userRepo.findById(username2).get();
        User newUser1 = userService.unfollowUser(user1, user2);

        if (newUser1 == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not follow the second user");
        }

        return ResponseEntity.status(HttpStatus.OK).body("User account unfollowed successfully");
    }

    /**
     * Gets the followers of a user.
     *
     * @param username The username of the user.
     * @return The followers of the user.
     */
    @GetMapping("/followers/{username}")
    public ResponseEntity getFollowers(@PathVariable String username) {
        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);

        try {
            handler.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is not valid");
        }

        User user = userRepo.findById(username).get();
        List<User> followers = userService.getFollowers(user);

        return ResponseEntity.status(HttpStatus.OK).body(followers);
    }

    /**
     * Get the users that a given user is following.
     *
     * @param username The username of the user.
     * @return The list of users they follow.
     */
    @GetMapping("/following/{username}")
    public ResponseEntity getFollowing(@PathVariable String username) {
        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);

        try {
            handler.handle(u);
        } catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is not valid");
        }

        User user = userRepo.findById(username).get();
        List<User> following = userService.getFollowing(user);

        return ResponseEntity.status(HttpStatus.OK).body(following);
    }
}
