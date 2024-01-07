package nl.tudelft.sem.template.example.controllers;

//import nl.tudelft.sem.template.api.UserApi;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepo;

    /*
    "/user/" route
     */
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody User loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Check if the username is valid
        if (!userRepo.existsById(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username/password supplied");
        }

        // Fetch the User instance from the database
        User currentUser = this.userRepo.findById(username).get();

        // Check if the password is correct
        if (!currentUser.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username/password supplied");
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
        currentUser.setIsLoggedIn(true);
        this.userRepo.saveAndFlush(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully");
    }

    @PostMapping("/logout/{username}")
    public ResponseEntity logoutUser(@PathVariable String username) {
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
        currentUser.setIsLoggedIn(false);
        this.userRepo.saveAndFlush(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body("User logged out successfully");
    }
    @PostMapping
    public ResponseEntity createUser(@RequestBody User newUser){
        String newUsername = newUser.getUsername();
        if(userRepo.existsById(newUsername)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already in use");
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
}
