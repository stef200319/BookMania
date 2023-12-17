package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.api.UserApi;
import nl.tudelft.sem.template.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    @Override
    public ResponseEntity<User> getUserByName(String username) {
        User u = new User();
        u.setUsername("a");
        return new ResponseEntity<User>(u, HttpStatus.OK);
    }
}
