package nl.tudelft.sem.template.example.authenticationStrategy;

import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.UserService;

public class AuthorAuthentication implements Authenticate{
    private UserService userService;

    private String username;

    public AuthorAuthentication(UserService userService, String username) {
        this.userService = userService;
        this.username = username;
    }
    @Override
    public boolean auth() {
        User user = userService.fetchUser(username);
        return user.getUserStatus().getUserRole().equals("Author");
    }
}
