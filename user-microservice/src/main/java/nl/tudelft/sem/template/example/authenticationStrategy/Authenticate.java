package nl.tudelft.sem.template.example.authenticationStrategy;

import org.springframework.stereotype.Service;

@Service
public interface Authenticate {
    boolean auth(String username);
}
