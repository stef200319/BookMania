package nl.tudelft.sem.template.example.analyticsValidators;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsCommentValidator;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyticsCommentValidatorTest {
    @Test
    void handle_validCommentsNumber_shouldReturnTrue() {
        AnalyticsCommentValidator commentValidator = new AnalyticsCommentValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setCommentsNumber(5L);

        boolean result;
        try {
            result = commentValidator.handle(analytics);
        } catch (InvalidAnalyticsException e) {
            return;
        }

        assertTrue(result);
    }

    @Test
    void handle_validCommentsNumber_shouldReturnTrueZero() {
        AnalyticsCommentValidator commentValidator = new AnalyticsCommentValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setCommentsNumber(0L);

        boolean result;
        try {
            result = commentValidator.handle(analytics);
            assertTrue(result);
        } catch (InvalidAnalyticsException e) {
            fail();
        }

    }

    @Test
    void handle_negativeCommentsNumber_shouldThrowInvalidDataException() {
        AnalyticsCommentValidator commentValidator = new AnalyticsCommentValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setCommentsNumber(-2L);

        assertThrows(InvalidAnalyticsException.class, () -> commentValidator.handle(analytics));
    }
}
