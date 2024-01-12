package nl.tudelft.sem.template.example.analyticsValidators;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsIDExistsValidator;
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
public class AnalyticsIDExistsValidatorTest {
    private AnalyticsRepository analyticsRepository;
    private AnalyticsIDExistsValidator idExistsValidator;

    @BeforeEach
    void setUp() {
        // Initialize and inject mocked dependencies
        analyticsRepository = Mockito.mock(AnalyticsRepository.class);
        idExistsValidator = new AnalyticsIDExistsValidator(analyticsRepository);
    }

    @Test
    void handle_existingID_shouldReturnTrue() {
        Analytics analytics = new Analytics("existingUser");
        when(analyticsRepository.existsById("existingUser")).thenReturn(true);
        boolean result;
        try {
            result = idExistsValidator.handle(analytics);
        } catch (InvalidAnalyticsException e) {
            return;
        }
        assertTrue(result);
    }

    @Test
    void handle_nonExistingID_shouldThrowInvalidAnalyticsException() {
        Analytics analytics = new Analytics("nonExistingUser");
        when(analyticsRepository.existsById("nonExistingUser")).thenReturn(false);
        assertThrows(InvalidAnalyticsException.class, () -> idExistsValidator.handle(analytics));
    }

}
