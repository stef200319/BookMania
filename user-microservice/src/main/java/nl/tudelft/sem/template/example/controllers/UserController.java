package nl.tudelft.sem.template.example.controllers;

import java.util.List;
import nl.tudelft.sem.template.api.UserApi;
import nl.tudelft.sem.template.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

  public ResponseEntity<List<User>> searchUser(String query, String searchBy, Boolean isAuthor) {

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
