package nl.tudelft.sem.template.example.analyticsValidators;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsFollowersValidator;
import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AnalyticsFollowersValidatorTest {
    @Test
    void handle_validFollowersNumber_shouldReturnTrue() {
        AnalyticsFollowersValidator followersValidator = new AnalyticsFollowersValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setFollowersNumber(10L);
        boolean result;
        try {
            result = followersValidator.handle(analytics);
        } catch (InvalidAnalyticsException e) {
            return;
        }
        assertTrue(result);
    }
    @Test
    void handle_validFollowersNumber_shouldReturnTrueZero() {
        AnalyticsFollowersValidator followersValidator = new AnalyticsFollowersValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setFollowersNumber(0L);
        boolean result;
        try {
            result = followersValidator.handle(analytics);
            assertTrue(result);
        } catch (InvalidAnalyticsException e) {
            fail();
        }
    }

    @Test
    void handle_negativeFollowersNumber_shouldThrowInvalidDataException() {
        AnalyticsFollowersValidator followersValidator = new AnalyticsFollowersValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setFollowersNumber(-5L);
        assertThrows(InvalidAnalyticsException.class, () -> followersValidator.handle(analytics));
    }
}
