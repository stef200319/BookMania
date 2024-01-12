package nl.tudelft.sem.template.example.controllers;

//import nl.tudelft.sem.template.api.UserApi;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Check if the username is valid
        if (!userRepo.existsById(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Fetch the User instance from the database
        User currentUser = this.userRepo.findById(username).get();

        // Check if the password is correct
        if (!currentUser.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password supplied");
        }

        // Check if the user account is active
        if (!currentUser.getIsActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User account is not active");
        }

        // Check if the user already logged in
        if(!(currentUser.getIsLoggedIn() == null)){
            if(currentUser.getIsLoggedIn()) {
                return ResponseEntity.status(HttpStatus.OK).body("User already logged in");
            }
        }

        // Set the user as logged in
        userService.logInUser(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully");
    }

    @PostMapping("/logout/{username}")
    public ResponseEntity logOutUser(@PathVariable String username) {
        // Check if the username is valid
        if (!userRepo.existsById(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Fetch the User instance from the DB
        User currentUser = this.userRepo.findById(username).get();

        // Check if the user not logged in
        if (currentUser.getIsLoggedIn() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        if (!currentUser.getIsLoggedIn()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        // Set the user as logged out
        userService.logOutUser(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body("User logged out successfully");
    }
    @PostMapping
    public ResponseEntity createUser(@RequestBody User newUser){
        String newUsername = newUser.getUsername();
        String newEmail = newUser.getEmail();

        // Check if the chosen username is available
        if(userRepo.existsById(newUsername)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already in use");
        }

        // Check if the username format is valid
        if(!newUsername.matches("^[a-zA-Z][a-zA-Z0-9]*")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username format. The username must contain only alphanumeric characters.");
        }

        // Check if the email address is valid
        if(!newEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }

        // Check if the User object is valid
        if(!newUser.getIsActive() || newUser.getIsBanned()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user object");
        }

        User savedUser = userRepo.saveAndFlush(newUser);
        return ResponseEntity.status(HttpStatus.OK).body(savedUser);
    }

    @PutMapping
    public ResponseEntity updateUserInfo(@RequestBody User modifiedUser){
        String username = modifiedUser.getUsername();
        // Check whether the username is valid
        if(!userRepo.existsById(username)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The user does not exist");
        }
        // Fetch the User instance from the DB
        User currentUser = this.userRepo.findById(username).get();
        // Check whether the sender of the request is logged in
        if(currentUser.getIsLoggedIn()){
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
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }
    }

    /*
    "/user/delete/{username}" route
     */
    @DeleteMapping("/delete/{username}")
    public ResponseEntity deleteSelf(@PathVariable String username){
        // Check if the provided username is valid
        if(!userRepo.existsById(username)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is not valid");
        }
        // Retrieve the User instance from the DB
        User currentUser = this.userRepo.findById(username).get();
        // Check whether the User is logged in
        if(!currentUser.getIsLoggedIn()){
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
        // Check if the username is valid
        if(!userRepo.existsById(username)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User fetchedUser = userRepo.getOne(username);
        return ResponseEntity.status(HttpStatus.OK).body(fetchedUser);
    }


    /*
    "/user/deactivate/{username}" route
     */
    @PutMapping("/deactivate/{username}")
    public ResponseEntity deactivateSelf(@PathVariable String username){
        // Check if the username is valid
        if(!this.userRepo.existsById(username)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is not valid");
        }
        // Retrieve the User instance from the DB
        User currentUser = this.userRepo.findById(username).get();
        // Check if the user is logged in
        if(!currentUser.getIsLoggedIn()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }
        // Check if the user account is already not active
        if(!currentUser.getIsActive()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User account is already inactive");
        }
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
        // Check if the username is valid
        if(!this.userRepo.existsById(username)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is not valid");
        }
        // Retrieve the User instance from the DB
        User currentUser = this.userRepo.findById(username).get();
        // Check if the user is logged in
        if(!currentUser.getIsLoggedIn()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }
        // Check if the user account is already active
        if(currentUser.getIsActive()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User account is already active");
        }
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
        // Check if the username of the admin is valid
        if(!this.userRepo.existsById(adminUsername)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the admin is not valid");
        }
        // Check if the admin is logged in
        User admin = this.userRepo.findById(adminUsername).get();
        if(!admin.getIsLoggedIn()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin is not logged in");
        }
        // Check if the admin is authorized
        if(!admin.getUserRole().equals(User.UserRoleEnum.ADMIN)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only an admin can perform this operation");
        }
        // Check if the username of the user to be modified is valid
        if(!userRepo.existsById(username)){
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
        // Check if the username of the admin is valid
        if(!this.userRepo.existsById(adminUsername)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the admin is not valid");
        }
        // Check if the admin is logged in
        User admin = this.userRepo.findById(adminUsername).get();
        if(!admin.getIsLoggedIn()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin is not logged in");
        }
        // Check if the admin is authorized
        if(!admin.getUserRole().equals(User.UserRoleEnum.ADMIN)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only an admin can perform this operation");
        }
        // Check if the username of the user to be modified is valid
        if(!userRepo.existsById(username)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        // Delete the user account
        userRepo.deleteById(username);
        return ResponseEntity.status(HttpStatus.OK).body("User account deleted successfully");
    }

    @PostMapping("/follow/{username1}/{username2}")
    public ResponseEntity followUser(@PathVariable String username1, @PathVariable String username2) {
        if(!this.userRepo.existsById(username1)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user executing the action is not valid");
        }

        if(!this.userRepo.existsById(username2)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user being followed is not valid");
        }

        User user1 = userRepo.findById(username1).get();
        User user2 = userRepo.findById(username2).get();

        if(!user1.getIsLoggedIn())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");

        userService.followUser(user1, user2);

        return ResponseEntity.status(HttpStatus.OK).body("User account followed successfully");
    }

    @DeleteMapping("/follow/{username1}/{username2}")
    public ResponseEntity unfollowUser(@PathVariable String username1, @PathVariable String username2) {
        if(!this.userRepo.existsById(username1)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user executing the action is not valid");
        }

        if(!this.userRepo.existsById(username2)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username of the user being unfollowed is not valid");
        }

        User user1 = userRepo.findById(username1).get();
        User user2 = userRepo.findById(username2).get();

        if(!user1.getIsLoggedIn())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");

        User newUser1 = userService.unfollowUser(user1, user2);

        if(newUser1 == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not follow the second user");

        return ResponseEntity.status(HttpStatus.OK).body("User account unfollowed successfully");
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity getFollowers(@PathVariable String username) {
        if(!this.userRepo.existsById(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is not valid");
        }

        User user = userRepo.findById(username).get();
        List<User> followers = user.getFollowers();
        return ResponseEntity.status(HttpStatus.OK).body(followers);
    }

    @GetMapping("/following/{username}")
    public ResponseEntity getFollowing(@PathVariable String username) {
        if(!this.userRepo.existsById(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is not valid");
        }

        User user = userRepo.findById(username).get();
        List<User> following = user.getFollowing();
        return ResponseEntity.status(HttpStatus.OK).body(following);
    }
}
