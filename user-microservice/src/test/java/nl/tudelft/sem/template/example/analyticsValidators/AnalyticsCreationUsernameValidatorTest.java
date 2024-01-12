package nl.tudelft.sem.template.example.analyticsValidators;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsCreationUsernameValidator;
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
public class AnalyticsCreationUsernameValidatorTest {
    private AnalyticsRepository analyticsRepository;
    private AnalyticsCreationUsernameValidator usernameValidator;

    @BeforeEach
    void setUp() {
        analyticsRepository = Mockito.mock(AnalyticsRepository.class);
        usernameValidator = new AnalyticsCreationUsernameValidator(analyticsRepository);
    }

    @Test
    void handle_newUsername_shouldReturnTrue() {
        Analytics analytics = new Analytics("newUser");
        when(analyticsRepository.findById("newUser")).thenReturn(Optional.empty());
        boolean result;
        try {
            result = usernameValidator.handle(analytics);
        } catch (InvalidAnalyticsException e) {
            return;
        }

        assertTrue(result);
    }

    @Test
    void handle_existingUsername_shouldThrowInvalidAnalyticsException() {
        // Arrange
        Analytics analytics = new Analytics("existingUser");

        // Mock behavior for analyticsRepository
        when(analyticsRepository.findById("existingUser")).thenReturn(Optional.of(analytics));

        // Act & Assert
        assertThrows(InvalidAnalyticsException.class, () -> usernameValidator.handle(analytics));
    }
}
