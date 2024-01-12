package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.analyticsHandlers.*;
import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.Analytics;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.AnalyticsService;
import nl.tudelft.sem.template.example.userHandlers.UserExistingValidator;
import nl.tudelft.sem.template.example.userHandlers.UsernameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Handler;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private final UserRepository userRepository;
    private final AnalyticsRepository analyticsRepository;

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(UserRepository userRepository, AnalyticsRepository analyticsRepository, AnalyticsService analyticsService) {
        this.userRepository = userRepository;
        this.analyticsRepository = analyticsRepository;
        this.analyticsService = analyticsService;
    }

    @PostMapping("/{username}")
    public ResponseEntity createAnalyticsEntity(@PathVariable String username, @RequestBody Analytics analytics) {
        if(!userRepository.existsById(username)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username does not exist.");

        if(analyticsRepository.existsById(username)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Analytics entity already exists.");

        Analytics savedAnalytics = analyticsService.createAnalytics(analytics);
        return ResponseEntity.status(HttpStatus.OK).body(savedAnalytics);
    }

    @PostMapping("/{username}")
    public ResponseEntity createAnalyticsEntityNew(@PathVariable String username) {
        UserExistingValidator h1 = new UserExistingValidator(userRepository);
        User dummy = new User();
        dummy.setUsername(username);

        try{
            h1.handle(dummy);
        } catch (InvalidUsernameException  | InvalidUserException  | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username does not exist.");
        }

        AnalyticsCreationUsernameValidator h2 = new AnalyticsCreationUsernameValidator(analyticsRepository);
        Analytics analytics = new Analytics(username);

        try {
            h2.handle(analytics);
        } catch (InvalidAnalyticsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Analytics entity already exists.");
        }

        Analytics savedAnalytics = analyticsService.createAnalytics(analytics);
        return ResponseEntity.status(HttpStatus.OK).body(savedAnalytics);
    }

    @PutMapping("/{username}")
    public ResponseEntity editAnalyticsEntity(@PathVariable String username, @RequestBody Analytics editedAnalytics) {
        UserExistingValidator h1 = new UserExistingValidator(userRepository);
        User dummy = new User();
        dummy.setUsername(username);

        try{
            h1.handle(dummy);
        } catch (InvalidUsernameException  | InvalidUserException  | InvalidEmailException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username does not exist.");
        }

        AnalyticsIDExistsValidator h = new AnalyticsIDExistsValidator(analyticsRepository);
        Analytics dumm = new Analytics(username);

        try {
            h.handle(dumm);
        } catch (InvalidAnalyticsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no analytics for this user.");
        }

        AnalyticsUsernameValidator h2 = new AnalyticsUsernameValidator(username, userRepository);
        AnalyticsReviewValidator h3 = new AnalyticsReviewValidator();
        AnalyticsCommentValidator h4 = new AnalyticsCommentValidator();
        AnalyticsFollowersValidator h5 = new AnalyticsFollowersValidator();
        AnalyticsFollowingValidator h6 = new AnalyticsFollowingValidator();
        AnalyticsLoginDateValidator h7 = new AnalyticsLoginDateValidator();
        h2.setNext(h3);
        h3.setNext(h4);
        h4.setNext(h5);
        h5.setNext(h6);
        h6.setNext(h7);

        try {
            h2.handle(editedAnalytics);
        } catch (InvalidAnalyticsException e) {
            switch(e.getMessage()) {
                case "The username of the edited data does not exist in the database.":
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username does not exist.");
                case "The username of the analytics entity does not match with the one passed as a parameter.":
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("The username of the analytics entity does not match with the one passed as a parameter.");
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A field of the analytics entity passed as a parameter is illegal.");

            }
        }

        Analytics analytics = analyticsService.editAnalytics(username, editedAnalytics);
        return ResponseEntity.status(HttpStatus.OK).body(analytics);
    }

    @GetMapping("/{username}")
    public ResponseEntity getAnalyticsEntity(@PathVariable String username) {
        AnalyticsIDExistsValidator h1 = new AnalyticsIDExistsValidator(analyticsRepository);
        Analytics dummy = new Analytics(username);

        try {
            h1.handle(dummy);
        } catch (InvalidAnalyticsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no analytics for this user.");
        }

        Analytics analytics = analyticsService.getAnalytics(username);
        return ResponseEntity.status(HttpStatus.OK).body(analytics);
    }

    @DeleteMapping
    public ResponseEntity deleteAnalyticsEntity(@PathVariable String username) {
        AnalyticsIDExistsValidator h1 = new AnalyticsIDExistsValidator(analyticsRepository);
        Analytics dummy = new Analytics(username);

        try {
            h1.handle(dummy);
        } catch (InvalidAnalyticsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no analytics for this user.");
        }

        Analytics deletedAnalytics = analyticsService.deleteAnalytics(username);
        return ResponseEntity.status(HttpStatus.OK).body(deletedAnalytics);
    }
}
