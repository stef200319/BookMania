package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.FollowService;
import nl.tudelft.sem.template.example.userHandlers.UserExistingValidator;
import nl.tudelft.sem.template.example.userHandlers.UserLoggedInValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/user")
public class FollowController {
    private final UserRepository userRepo;
    private final FollowService followService;

    /**
     * Create a user controller.
     *
     * @param userRepo The repository to save the user to.
     * @param followService The service that handles follow logic
     */
    @Autowired
    public FollowController(UserRepository userRepo, FollowService followService) {
        this.userRepo = userRepo;
        this.followService = followService;
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

        followService.followUser(user1, user2);

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
        User newUser1 = followService.unfollowUser(user1, user2);

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
        List<User> followers = followService.getFollowers(user);

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
        List<User> following = followService.getFollowing(user);

        return ResponseEntity.status(HttpStatus.OK).body(following);
    }
}
