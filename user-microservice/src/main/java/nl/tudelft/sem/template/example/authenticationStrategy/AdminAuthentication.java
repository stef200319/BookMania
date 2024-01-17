package nl.tudelft.sem.template.example.authenticationStrategy;

import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.UserService;

import java.util.NoSuchElementException;

public class AdminAuthentication implements Authenticate{
    private UserService userService;

    public AdminAuthentication(UserService userService) {
        this.userService = userService;
    }
    @Override
    public boolean auth(String username) {
        User user;
        try {
            user = userService.fetchUser(username);
        } catch (NoSuchElementException e) {
            return false;
        }
        return (user.getUserStatus().getUserRole().getValue().equals("Admin"));
    }
}
