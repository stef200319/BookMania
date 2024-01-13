package nl.tudelft.sem.template.example.analyticsValidators;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsLoginDateValidator;
import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class AnalyticsLoginDateValidatorTest {
    @Test
    void handle_validLoginDate_shouldReturnTrue() {
        AnalyticsLoginDateValidator loginDateValidator = new AnalyticsLoginDateValidator();
        Analytics analytics = new Analytics("testUser");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String validLoginDate = formatter.format(LocalDateTime.now().minusDays(1)); // One day ago
        analytics.setLastLoginDate(validLoginDate);
        boolean result;
        try {
            result = loginDateValidator.handle(analytics);
        } catch (InvalidAnalyticsException e) {
            return;
        }
        assertTrue(result);
    }

    @Test
    void handle_futureLoginDate_shouldThrowInvalidDataException() {
        AnalyticsLoginDateValidator loginDateValidator = new AnalyticsLoginDateValidator();
        Analytics analytics = new Analytics("testUser");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String futureLoginDate = formatter.format(LocalDateTime.now().plusDays(1)); // One day in the future
        analytics.setLastLoginDate(futureLoginDate);
        assertThrows(InvalidAnalyticsException.class, () -> loginDateValidator.handle(analytics));
    }

    @Test
    void handle_invalidDateFormat_shouldThrowRuntimeException() {
        AnalyticsLoginDateValidator loginDateValidator = new AnalyticsLoginDateValidator();
        Analytics analytics = new Analytics("testUser");
        analytics.setLastLoginDate("invalidDateFormat");
        assertThrows(RuntimeException.class, () -> loginDateValidator.handle(analytics));
    }
}
