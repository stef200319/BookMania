package nl.tudelft.sem.template.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Getter
@Setter
public class LoginRequest {
    private String username;

    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
