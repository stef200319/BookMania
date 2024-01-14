package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.database.UserStatusRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userStatus")
public class UserStatusController {
    private UserStatusRepository userStatusRepository;

    private UserStatusService userStatusService;

    @PostMapping("/{username}")
    public ResponseEntity createUserMapping(@PathVariable String username) {

    }
}
