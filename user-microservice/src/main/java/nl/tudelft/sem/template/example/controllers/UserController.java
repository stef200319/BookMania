package nl.tudelft.sem.template.example.controllers;

//import nl.tudelft.sem.template.api.UserApi;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.services.UserService;
import nl.tudelft.sem.template.example.userHandlers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Handler;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepo;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    /*
    "/user/" route
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
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            if(e.getMessage().equals("Wrong password"))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password supplied");
            if(e.getMessage().equals("User is inactive"))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User account is not active");
            if(e.getMessage().equals("User is logged in"))
                return ResponseEntity.status(HttpStatus.OK).body("User already logged in");
        }

        User currentUser = this.userRepo.findById(loginRequest.getUsername()).get();
        userService.logInUser(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully");
    }

    @PostMapping("/logout/{username}")
    public ResponseEntity logOutUser(@PathVariable String username) {
        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);
        handler.setNext(new UserLoggedInValidator(userRepo));

        try {
            handler.handle(u);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            if(e.getMessage().equals("User is not logged in"))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        // Set the user as logged out
        User currentUser = this.userRepo.findById(username).get();
        userService.logOutUser(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body("User logged out successfully");
    }


    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody User newUser){
//        String newUsername = newUser.getUsername();
//        String newEmail = newUser.getEmail();
//
//        // Check if the chosen username is available
//        if(userRepo.existsById(newUsername)){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already in use");
//        }
//
//        // Check if the username format is valid
//        if(!newUsername.matches("^[a-zA-Z][a-zA-Z0-9]*")){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username format. The username must contain only alphanumeric characters.");
//        }
//
//        // Check if the email address is valid
//        if(!newEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$")){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
//        }
//
//        // Check if the User object is valid
//        if(!newUser.getIsActive() || newUser.getIsBanned()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user object");
//        }

        UsernameValidator handler = new UsernameValidator(userRepo);
        EmailValidator ev = new EmailValidator(userRepo);
        UserActiveValidator av = new UserActiveValidator(userRepo);
        av.setNext(new UserBannedValidator(userRepo));
        ev.setNext(av);
        handler.setNext(ev);

        try {
            handler.handle(newUser);
        }
        catch (InvalidUsernameException e) {
            if(e.getMessage().equals("Username already in use"))
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username format. The username must contain only alphanumeric characters.");
        }
        catch (InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }
        catch (InvalidUserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user object");
        }

        User savedUser = userRepo.saveAndFlush(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(savedUser);
    }

    @PutMapping
    public ResponseEntity updateUserInfo(@RequestBody User modifiedUser){
//        String username = modifiedUser.getUsername();
//        // Check whether the username is valid
//        if(!userRepo.existsById(username)){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The user does not exist");
//        }
//        // Fetch the User instance from the DB
//        User currentUser = this.userRepo.findById(username).get();
//        // Check whether the sender of the request is logged in
//        if(currentUser.getIsLoggedIn()){
//            // Perform modifications of the personal info of the user
//            currentUser.setBio(modifiedUser.getBio());
//            currentUser.setFirstName(modifiedUser.getFirstName());
//            currentUser.setLastName(modifiedUser.getLastName());
//            currentUser.setProfilePicture(modifiedUser.getProfilePicture());
//            currentUser.setLocation(modifiedUser.getLocation());
//            // Check if the email address is valid
//            if(!modifiedUser.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$")){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
//            }
//            currentUser.setEmail(modifiedUser.getEmail());
//            currentUser.setPassword(modifiedUser.getPassword());
//            currentUser.setFavoriteBook(modifiedUser.getFavoriteBook());
//            currentUser.setFavoriteGenres(modifiedUser.getFavoriteGenres());
//            // Persist the changes in the DB
//            this.userRepo.saveAndFlush(currentUser);
//            return ResponseEntity.status(HttpStatus.OK).body("Account updated successfully");
//        }
//        else{
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
//        }

        EmailValidator handler = new EmailValidator(userRepo);
        UserExistingValidator ev =  new UserExistingValidator(userRepo);
        ev.setNext(new UserLoggedInValidator(userRepo));
        handler.setNext(ev);

        try {
            handler.handle(modifiedUser);
        }
        catch (InvalidUsernameException | InvalidUserException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The user does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }
        catch (InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }

        User currentUser = this.userRepo.findById(modifiedUser.getUsername()).get();
        // Perform modifications of the personal info of the user
        currentUser.setBio(modifiedUser.getBio());
        currentUser.setFirstName(modifiedUser.getFirstName());
        currentUser.setLastName(modifiedUser.getLastName());
        currentUser.setProfilePicture(modifiedUser.getProfilePicture());
        currentUser.setLocation(modifiedUser.getLocation());
        // Check if the email address is valid
        if(!modifiedUser.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }
        currentUser.setEmail(modifiedUser.getEmail());
        currentUser.setPassword(modifiedUser.getPassword());
        currentUser.setFavoriteBook(modifiedUser.getFavoriteBook());
        currentUser.setFavoriteGenres(modifiedUser.getFavoriteGenres());
        // Persist the changes in the DB
        this.userRepo.saveAndFlush(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body("Account updated successfully");
    }

    /*
    "/user/delete/{username}" route
     */
    @DeleteMapping("/delete/{username}")
    public ResponseEntity deleteSelf(@PathVariable String username){

        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);    // Check if the provided username is valid
        handler.setNext(new UserLoggedInValidator(userRepo));                   // Check whether the User is logged in

        try {
            handler.handle(u);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is not valid");
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }

        // Remove the User entity from the DB
        this.userRepo.deleteById(username);
        return ResponseEntity.status(HttpStatus.OK).body("Account deleted successfully");

    }

    /*
    "/user/{username}" route
     */
    @GetMapping("/{username}")
    public ResponseEntity getUserByUsername(@PathVariable String username){
        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);

        try {
            handler.handle(u);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User fetchedUser = userRepo.findById(username).get();
        return ResponseEntity.status(HttpStatus.OK).body(fetchedUser);
    }


    /*
    "/user/deactivate/{username}" route
     */
    @PutMapping("/deactivate/{username}")
    public ResponseEntity deactivateSelf(@PathVariable String username){

        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);
        UserLoggedInValidator liv = new UserLoggedInValidator(userRepo);
        liv.setNext(new UserActiveValidator(userRepo));
        handler.setNext(liv);

        try {
            handler.handle(u);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is not valid");
            if(e.getMessage().equals("User is not logged in"))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
            if(e.getMessage().equals("User is inactive"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User account is already inactive");
        }

        User currentUser = userRepo.findById(username).get();

        // Modify the activation status of the user
        currentUser.setIsActive(false);
        // Persist the changes to the DB
        this.userRepo.saveAndFlush(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body("User account deactivated successfully");
    }

    /*
    "/user/reactivate/{username}" route
     */
    @PutMapping("/reactivate/{username}")
    public ResponseEntity reactivateSelf(@PathVariable String username){

        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);
        UserLoggedInValidator liv = new UserLoggedInValidator(userRepo);
        liv.setNext(new UserAlreadyActiveValidator(userRepo));
        handler.setNext(liv);

        try {
            handler.handle(u);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is not valid");
            if(e.getMessage().equals("User is not logged in"))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
            if(e.getMessage().equals("User is active"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User account is already active");
        }

        User currentUser = this.userRepo.findById(username).get();
        // Modify the activation status of the user
        currentUser.setIsActive(true);
        // Persist the changes to the DB
        this.userRepo.saveAndFlush(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body("User account reactivated successfully");
    }


    /*
    "/user/setActive/{adminUsername}/{username}" route
     */
    @PutMapping("/setActive/{adminUsername}/{username}")
    public ResponseEntity changeActivation(@PathVariable String adminUsername, @PathVariable String username, @RequestBody boolean flag){
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
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the admin is not valid");
            if(e.getMessage().equals("User is not logged in"))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin is not logged in");
            if(e.getMessage().equals("User is not an admin"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only an admin can perform this operation");
        }

        UserExistingValidator handlerUser = new UserExistingValidator(userRepo);

        try {
            handlerUser.handle(u);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Modify the activation status of the user
        User currentUser = userRepo.findById(username).get();
        currentUser.setIsActive(flag);
        userRepo.saveAndFlush(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body("Activation status changed successfully");
    }

    /*
    "/user/delete/{adminUsername}/{username}"
     */
    @DeleteMapping("/delete/{adminUsername}/{username}")
    public ResponseEntity deleteUser(@PathVariable String adminUsername,@PathVariable String username){
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
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the admin is not valid");
            if(e.getMessage().equals("User is not logged in"))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin is not logged in");
            if(e.getMessage().equals("User is not an admin"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only an admin can perform this operation");
        }

        UserExistingValidator handlerUser = new UserExistingValidator(userRepo);

        try {
            handlerUser.handle(u);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Delete the user account
        userRepo.deleteById(username);
        return ResponseEntity.status(HttpStatus.OK).body("User account deleted successfully");
    }

    @PostMapping("/follow/{username1}/{username2}")
    public ResponseEntity followUser(@PathVariable String username1, @PathVariable String username2) {
        User u1 = new User();
        u1.setUsername(username1);

        User u2 = new User();
        u2.setUsername(username2);

        UserExistingValidator handler = new UserExistingValidator(userRepo);

        try {
            handler.handle(u2);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user being followed is not valid");
        }

        handler.setNext(new UserLoggedInValidator(userRepo));

        try {
            handler.handle(u1);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user executing the action is not valid");
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }

        User user1 = userRepo.findById(username1).get();
        User user2 = userRepo.findById(username2).get();

        userService.followUser(user1, user2);

        return ResponseEntity.status(HttpStatus.OK).body("User account followed successfully");
    }

    @DeleteMapping("/follow/{username1}/{username2}")
    public ResponseEntity unfollowUser(@PathVariable String username1, @PathVariable String username2) {
//        if(!this.userRepo.existsById(username1)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user executing the action is not valid");
//        }
//
//        if(!this.userRepo.existsById(username2)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user being unfollowed is not valid");
//        }
//

//
//        if(!user1.getIsLoggedIn())
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        User u1 = new User();
        u1.setUsername(username1);

        User u2 = new User();
        u2.setUsername(username2);

        UserExistingValidator handler1 = new UserExistingValidator(userRepo);
        handler1.setNext(new UserLoggedInValidator(userRepo));
        UserExistingValidator handler2 = new UserExistingValidator(userRepo);

        try {
            handler1.handle(u1);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            if(e.getMessage().equals("User does not exist"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user executing the action is not valid");
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }

        try {
            handler2.handle(u2);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user being unfollowed is not valid");
        }

        User user1 = userRepo.findById(username1).get();
        User user2 = userRepo.findById(username2).get();
        User newUser1 = userService.unfollowUser(user1, user2);

        if(newUser1 == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not follow the second user");

        return ResponseEntity.status(HttpStatus.OK).body("User account unfollowed successfully");
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity getFollowers(@PathVariable String username) {
        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);

        try {
            handler.handle(u);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is not valid");
        }

        User user = userRepo.findById(username).get();
        List<User> followers = user.getFollowers();
        return ResponseEntity.status(HttpStatus.OK).body(followers);
    }

    @GetMapping("/following/{username}")
    public ResponseEntity getFollowing(@PathVariable String username) {
        User u = new User();
        u.setUsername(username);

        UserExistingValidator handler = new UserExistingValidator(userRepo);

        try {
            handler.handle(u);
        }
        catch (InvalidUserException | InvalidUsernameException | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is not valid");
        }

        User user = userRepo.findById(username).get();
        List<User> following = user.getFollowing();
        return ResponseEntity.status(HttpStatus.OK).body(following);
    }
}
