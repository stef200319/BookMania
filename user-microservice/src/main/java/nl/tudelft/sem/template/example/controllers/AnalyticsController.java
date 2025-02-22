package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsCommentValidator;
import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsCreationUsernameValidator;
import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsFollowersValidator;
import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsFollowingValidator;
import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsIDExistsValidator;
import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsLoginDateValidator;
import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsReviewValidator;
import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsUsernameValidator;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")

public class AnalyticsController {
    private transient final UserRepository userRepository;
    private transient final AnalyticsRepository analyticsRepository;

    private transient final AnalyticsService analyticsService;

    /**
     * Creates an analytics controller.
     *
     * @param userRepository The user repository.
     * @param analyticsRepository The analytics repository.
     * @param analyticsService The analytics service to handle all the logic.
     */
    @Autowired
    public AnalyticsController(
        UserRepository userRepository,
        AnalyticsRepository analyticsRepository,
        AnalyticsService analyticsService
    ) {
        this.userRepository = userRepository;
        this.analyticsRepository = analyticsRepository;
        this.analyticsService = analyticsService;
    }

    /**
     * Creates an analytics entity from an existing entity.
     *
     * @param username The user for which to create the analytics entity.
     * @param analytics The entity to create.
     * @return Either an error message or the created entity.
     */
    @PostMapping("/{username}")
    public ResponseEntity createAnalyticsEntity(
        @PathVariable String username,
        @RequestBody Analytics analytics
    ) {
        if (!userRepository.existsById(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username does not exist.");
        }

        if (analyticsRepository.existsById(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Analytics entity already exists.");
        }

        Analytics savedAnalytics = analyticsService.createAnalytics(analytics);
        return ResponseEntity.status(HttpStatus.OK).body(savedAnalytics);
    }

    /**
     * Creates a new analytics entity.
     *
     * @param username The user for which to create the analytics entity.
     * @return Either an error message or the created entity.
     */
    @PostMapping("/{username}/new")
    public ResponseEntity createAnalyticsEntityNew(@PathVariable String username) {
        UserExistingValidator h1 = new UserExistingValidator(userRepository);
        User dummy = new User();
        dummy.setUsername(username);

        try {
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
        return ResponseEntity.status(HttpStatus.OK).body(analytics);
    }

    /**
     * Edit an existing analytics entity.
     *
     * @param username The user for which the entity exists.
     * @param editedAnalytics The modified version of the entity.
     * @return The new entity values.
     */
    @PutMapping("/{username}")
    public ResponseEntity editAnalyticsEntity(@PathVariable String username, @RequestBody Analytics editedAnalytics) {
        UserExistingValidator h1 = new UserExistingValidator(userRepository);
        User dummy = new User();
        dummy.setUsername(username);

        try {
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
        h2.setNext(h3);
        AnalyticsCommentValidator h4 = new AnalyticsCommentValidator();
        h3.setNext(h4);
        AnalyticsFollowersValidator h5 = new AnalyticsFollowersValidator();
        h4.setNext(h5);
        AnalyticsFollowingValidator h6 = new AnalyticsFollowingValidator();
        h5.setNext(h6);
        AnalyticsLoginDateValidator h7 = new AnalyticsLoginDateValidator();
        h6.setNext(h7);

        try {
            h2.handle(editedAnalytics);
        } catch (InvalidAnalyticsException e) {
            return switch (e.getMessage()) {
                case "The username of the edited data does not exist in the database." ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Username does not exist.");
                case "The username of the analytics entity does not match with the one passed as a parameter." ->
                    ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("The username of the analytics entity does not match with the one passed as a parameter.");
                default ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("A field of the analytics entity passed as a parameter is illegal.");
            };
        }

        analyticsService.editAnalytics(username, editedAnalytics);
        return ResponseEntity.status(HttpStatus.OK).body(editedAnalytics);
    }

    /**
     * Get an existing analytics entity.
     *
     * @param username The user for which the entity exists.
     * @return Either an error or the entity for the user.
     */
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

    /**
     * Delete an analytics entity.
     *
     * @param username The user of the analytics entity.
     * @return Either an error or deleted analytics entity.
     */
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
