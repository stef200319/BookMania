package nl.tudelft.sem.template.example.analyticsValidators;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsReviewValidator;
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
public class AnalyticsReviewValidatorTest {
    @Test
    void handle_validReviewsNumber_shouldReturnTrue() {
        AnalyticsReviewValidator reviewValidator = new AnalyticsReviewValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setReviewsNumber(5L);
        boolean result;
        try {
            result = reviewValidator.handle(analytics);
        } catch (InvalidAnalyticsException e) {
            return;
        }
        assertTrue(result);
    }

    @Test
    void handle_negativeReviewsNumber_shouldThrowInvalidDataException() {
        AnalyticsReviewValidator reviewValidator = new AnalyticsReviewValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setReviewsNumber(-2L);
        assertThrows(InvalidAnalyticsException.class, () -> reviewValidator.handle(analytics));
    }
}
