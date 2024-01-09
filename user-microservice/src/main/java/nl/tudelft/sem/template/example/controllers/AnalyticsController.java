package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.api.AnalyticsApi;
import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import nl.tudelft.sem.template.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController implements AnalyticsApi {
    private final UserRepository userRepository;
    private final AnalyticsRepository analyticsRepository;

    @Autowired
    public AnalyticsController(UserRepository userRepository, AnalyticsRepository analyticsRepository) {
        this.userRepository = userRepository;
        this.analyticsRepository = analyticsRepository;
    }

    @PostMapping("/{username}")
    public ResponseEntity createAnalyticsEntity(@PathVariable String username, @RequestBody Analytics analytics) {
        if(!userRepository.existsById(username)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username does not exist.");

        if(analyticsRepository.existsById(username)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Analytics entity already exists.");

        Analytics savedAnalytics = analyticsRepository.saveAndFlush(analytics);
        return ResponseEntity.status(HttpStatus.OK).body(savedAnalytics);
    }

    @PutMapping("/{username}")
    public ResponseEntity editAnalyticsEntity(@PathVariable String username, @RequestBody Analytics editedAnalytics) {
        if(!analyticsRepository.existsById(username)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Analytics entity does not exist.");

        Analytics currentAnalytics = this.analyticsRepository.findById(username).get();
        currentAnalytics.setUserUsername(editedAnalytics.getUserUsername());
        currentAnalytics.setCommentsNumber(editedAnalytics.getCommentsNumber());
        currentAnalytics.setReviewsNumber(editedAnalytics.getReviewsNumber());
        currentAnalytics.setFollowingNumber(editedAnalytics.getFollowingNumber());
        currentAnalytics.setFollowersNumber(editedAnalytics.getFollowersNumber());
        currentAnalytics.setLastLoginDate(editedAnalytics.getLastLoginDate());
        this.analyticsRepository.saveAndFlush(currentAnalytics);

        return ResponseEntity.status(HttpStatus.OK).body(currentAnalytics);
    }

    @GetMapping("/{username}")
    public ResponseEntity getAnalyticsEntity(@PathVariable String username) {
        if(!analyticsRepository.existsById(username)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Analytics entity does not exist.");

        Analytics analytics = this.analyticsRepository.findById(username).get();
        return ResponseEntity.status(HttpStatus.OK).body(analytics);
    }

    @DeleteMapping
    public ResponseEntity deleteAnalyticsEntity(@PathVariable String username) {
        if(!analyticsRepository.existsById(username)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Analytics entity does not exist.");

        Analytics deletedAnalytics = this.analyticsRepository.findById(username).get();
        this.userRepository.deleteById(username);
        return ResponseEntity.status(HttpStatus.OK).body(deletedAnalytics);
    }
}
