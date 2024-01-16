package nl.tudelft.sem.template.example.authenticationStrategy;

import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.UserService;

import java.util.NoSuchElementException;

public class AdminAuthentication implements Authenticate{
    private UserService userService;

    private String username;

    public AdminAuthentication(UserService userService, String username) {
        this.userService = userService;
        this.username = username;
    }
    @Override
    public boolean auth() {
        User user;
        try {
            user = userService.fetchUser(username);
        } catch (NoSuchElementException e) {
            return false;
        }
        return (user.getUserStatus().getUserRole().equals("Admin"));
    }
}
