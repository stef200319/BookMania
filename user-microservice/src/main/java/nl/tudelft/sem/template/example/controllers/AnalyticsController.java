package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsIDExistsValidator;
import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import nl.tudelft.sem.template.example.services.AnalyticsService;
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

    @PutMapping("/{username}")
    public ResponseEntity editAnalyticsEntity(@PathVariable String username, @RequestBody Analytics editedAnalytics) {
        if(!analyticsRepository.existsById(username)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Analytics entity does not exist.");

        Analytics analytics = analyticsService.editAnalytics(username, editedAnalytics);
        return ResponseEntity.status(HttpStatus.OK).body(analytics);
    }

    @GetMapping("/{username}")
    public ResponseEntity getAnalyticsEntity(@PathVariable String username) {
        if(!analyticsRepository.existsById(username)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Analytics entity does not exist.");

        Analytics analytics = analyticsService.getAnalytics(username);
        return ResponseEntity.status(HttpStatus.OK).body(analytics);
    }

    @DeleteMapping
    public ResponseEntity deleteAnalyticsEntity(@PathVariable String username) {
        if(!analyticsRepository.existsById(username)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Analytics entity does not exist.");

        Analytics deletedAnalytics = analyticsService.deleteAnalytics(username);
        return ResponseEntity.status(HttpStatus.OK).body(deletedAnalytics);
    }
}
