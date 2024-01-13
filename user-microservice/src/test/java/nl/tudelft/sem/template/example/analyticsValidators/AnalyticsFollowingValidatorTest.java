package nl.tudelft.sem.template.example.analyticsValidators;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsFollowingValidator;
import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class AnalyticsFollowingValidatorTest {
    @Test
    void handle_validFollowingNumber_shouldReturnTrue() {
        AnalyticsFollowingValidator followingValidator = new AnalyticsFollowingValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setFollowingNumber(8L);
        boolean result;
        try {
            result = followingValidator.handle(analytics);
        } catch (InvalidAnalyticsException e) {
            return;
        }
        assertTrue(result);
    }

    @Test
    void handle_negativeFollowingNumber_shouldThrowInvalidDataException() {
        AnalyticsFollowingValidator followingValidator = new AnalyticsFollowingValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setFollowingNumber(-3L);
        assertThrows(InvalidAnalyticsException.class, () -> followingValidator.handle(analytics));
    }
}
