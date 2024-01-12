package nl.tudelft.sem.template.example;

import nl.tudelft.sem.template.example.controllers.AnalyticsController;
import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.services.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class AnalyticsControllerTest {
    private AnalyticsController analyticsController;
    private AnalyticsRepository analyticsRepository;
    private UserRepository userRepository;
    private AnalyticsService analyticsService;
    @BeforeEach
    void setUp() {
        analyticsRepository = Mockito.mock(AnalyticsRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        analyticsService = Mockito.mock(AnalyticsService.class);
        analyticsController = new AnalyticsController(userRepository, analyticsRepository, analyticsService);
    }

    @Test
    void createAnalyticsInvalidUserTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");

        Mockito.when(userRepository.existsById(username)).thenReturn(false);

        ResponseEntity response = analyticsController.createAnalyticsEntity(username, analytics);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Username does not exist.", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
    }

    @Test
    void existingAnalyticsTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");

        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(analyticsRepository.existsById(username)).thenReturn(true);

        ResponseEntity response = analyticsController.createAnalyticsEntity(username, analytics);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Analytics entity already exists.", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(analyticsRepository, Mockito.times(1)).existsById(username);
    }

    @Test
    void editAnalyticsBadTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");

        Mockito.when(analyticsRepository.existsById(username)).thenReturn(false);

        ResponseEntity response = analyticsController.editAnalyticsEntity(username, analytics);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Username does not exist.", response.getBody());

        Mockito.verify(analyticsRepository, Mockito.times(0)).findById(username);
    }

    @Test
    void getAnalyticsBadTest() {
        String username = "testUsername";

        // Analytics analytics = new Analytics(username, "now");

        Mockito.when(analyticsRepository.existsById(username)).thenReturn(false);

        ResponseEntity response = analyticsController.getAnalyticsEntity(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There are no analytics for this user.", response.getBody());

        Mockito.verify(analyticsRepository, Mockito.times(1)).existsById(username);
    }

    @Test
    void deleteAnalyticsBadTest() {
        String username = "testUsername";

        // Analytics analytics = new Analytics(username, "now");

        Mockito.when(analyticsRepository.existsById(username)).thenReturn(false);

        ResponseEntity response = analyticsController.deleteAnalyticsEntity(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There are no analytics for this user.", response.getBody());

        Mockito.verify(analyticsRepository, Mockito.times(1)).existsById(username);
    }

    @Test
    void createAnalyticsValidUserTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");

        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(analyticsRepository.existsById(username)).thenReturn(false);
        Mockito.when(analyticsRepository.saveAndFlush(any())).thenReturn(analytics);
        Mockito.when(analyticsService.createAnalytics(any())).thenReturn(analytics);

        ResponseEntity response = analyticsController.createAnalyticsEntity(username, analytics);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(analytics, response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(analyticsRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(analyticsRepository, Mockito.times(0)).saveAndFlush(any());
        Mockito.verify(analyticsService, Mockito.times(1)).createAnalytics(any());
    }

    @Test
    void editAnalyticsConflictTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");
        Analytics analytics1 = new Analytics("username1", "beforenow");

        User dummy = new User();

        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(dummy));
        Mockito.when(userRepository.existsById("username1")).thenReturn(true);
        Mockito.when(analyticsRepository.existsById(username)).thenReturn(true);
        Mockito.when(analyticsRepository.findById(username)).thenReturn(Optional.of(analytics));
        Mockito.when(analyticsRepository.saveAndFlush(any())).thenReturn(analytics1);
        Mockito.when(analyticsService.editAnalytics(username, analytics1)).thenReturn(analytics1);


        ResponseEntity response = analyticsController.editAnalyticsEntity(username, analytics1);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("The username of the analytics entity does not match with the one passed as a parameter.", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.times(1)).existsById("username1");
        // Mockito.verify(analyticsRepository, Mockito.times(1)).findById(username);
        // Mockito.verify(analyticsRepository, Mockito.times(1)).findById("username1");
        Mockito.verify(analyticsRepository, Mockito.times(0)).saveAndFlush(any());
        Mockito.verify(analyticsService, Mockito.times(0)).editAnalytics(username, analytics1);
    }

    @Test
    void editAnalyticsBadUsernameTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");
        Analytics analytics1 = new Analytics("username1", "beforenow");

        User dummy = new User();

        Mockito.when(userRepository.existsById(username)).thenReturn(false);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(dummy));
        Mockito.when(userRepository.existsById("username1")).thenReturn(true);
        Mockito.when(analyticsRepository.existsById(username)).thenReturn(true);
        Mockito.when(analyticsRepository.findById(username)).thenReturn(Optional.of(analytics));
        Mockito.when(analyticsRepository.saveAndFlush(any())).thenReturn(analytics1);
        Mockito.when(analyticsService.editAnalytics(username, analytics1)).thenReturn(analytics1);


        ResponseEntity response = analyticsController.editAnalyticsEntity(username, analytics1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Username does not exist.", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.times(0)).existsById("username1");
        // Mockito.verify(analyticsRepository, Mockito.times(1)).findById(username);
        // Mockito.verify(analyticsRepository, Mockito.times(1)).findById("username1");
        Mockito.verify(analyticsRepository, Mockito.times(0)).saveAndFlush(any());
        Mockito.verify(analyticsService, Mockito.times(0)).editAnalytics(username, analytics1);
    }

    @Test
    void editAnalyticsNoAnalyticsTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");
        Analytics analytics1 = new Analytics("username1", "beforenow");

        User dummy = new User();

        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(dummy));
        Mockito.when(analyticsRepository.existsById("username1")).thenReturn(false);
        Mockito.when(analyticsRepository.existsById(username)).thenReturn(false);
        Mockito.when(analyticsRepository.findById(username)).thenReturn(Optional.of(analytics));
        Mockito.when(analyticsRepository.saveAndFlush(any())).thenReturn(analytics1);
        Mockito.when(analyticsService.editAnalytics(username, analytics1)).thenReturn(analytics1);


        ResponseEntity response = analyticsController.editAnalyticsEntity(username, analytics1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There are no analytics for this user.", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.times(0)).existsById("username1");
        // Mockito.verify(analyticsRepository, Mockito.times(1)).findById(username);
        // Mockito.verify(analyticsRepository, Mockito.times(1)).findById("username1");
        Mockito.verify(analyticsRepository, Mockito.times(0)).saveAndFlush(any());
        Mockito.verify(analyticsService, Mockito.times(0)).editAnalytics(username, analytics1);
    }

    @Test
    void editAnalyticsNoAnalyticsInDatabaseTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");
        Analytics analytics1 = new Analytics("username1", "beforenow");

        User dummy = new User();

        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        Mockito.when(userRepository.findById(username)).thenReturn(Optional.of(dummy));
        Mockito.when(analyticsRepository.existsById("username1")).thenReturn(false);
        Mockito.when(analyticsRepository.existsById(username)).thenReturn(true);
        Mockito.when(analyticsRepository.findById(username)).thenReturn(Optional.of(analytics));
        Mockito.when(analyticsRepository.saveAndFlush(any())).thenReturn(analytics1);
        Mockito.when(analyticsService.editAnalytics(username, analytics1)).thenReturn(analytics1);


        ResponseEntity response = analyticsController.editAnalyticsEntity(username, analytics1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Username does not exist.", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(userRepository, Mockito.times(1)).existsById("username1");
        // Mockito.verify(analyticsRepository, Mockito.times(1)).findById(username);
        // Mockito.verify(analyticsRepository, Mockito.times(1)).findById("username1");
        Mockito.verify(analyticsRepository, Mockito.times(0)).saveAndFlush(any());
        Mockito.verify(analyticsService, Mockito.times(0)).editAnalytics(username, analytics1);
    }

    @Test
    void getAnalyticsTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");

        Mockito.when(analyticsRepository.existsById(username)).thenReturn(true);
        Mockito.when(analyticsRepository.findById(username)).thenReturn(Optional.of(analytics));
        Mockito.when(analyticsService.getAnalytics(username)).thenReturn(analytics);

        ResponseEntity response = analyticsController.getAnalyticsEntity(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(analytics, response.getBody());

        Mockito.verify(analyticsRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(analyticsService, Mockito.times(1)).getAnalytics(username);
    }

    @Test
    void deleteAnalyticsTest() {
        String username = "testUsername";

        Analytics analytics = new Analytics(username, "now");

        Mockito.when(analyticsRepository.existsById(username)).thenReturn(true);
        Mockito.when(analyticsRepository.findById(username)).thenReturn(Optional.of(analytics));
        Mockito.when(analyticsService.deleteAnalytics(username)).thenReturn(analytics);

        ResponseEntity response = analyticsController.deleteAnalyticsEntity(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(analytics, response.getBody());

        Mockito.verify(analyticsRepository, Mockito.times(1)).existsById(username);
        Mockito.verify(analyticsService, Mockito.times(1)).deleteAnalytics(username);
    }

    @Test
    void createNewTest() {
        String username = "testusername";

        Mockito.when(userRepository.existsById(username)).thenReturn(false);

        Analytics analytics = new Analytics(username);

        ResponseEntity response = analyticsController.createAnalyticsEntityNew(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Username does not exist.", response.getBody());

        Mockito.verify(userRepository, Mockito.times(1)).existsById(username);
    }
}
