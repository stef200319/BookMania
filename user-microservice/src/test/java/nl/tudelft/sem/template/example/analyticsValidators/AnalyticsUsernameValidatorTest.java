package nl.tudelft.sem.template.example.analyticsValidators;

import nl.tudelft.sem.template.example.analyticsHandlers.AnalyticsUsernameValidator;
import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.exceptions.InvalidDataException;
import nl.tudelft.sem.template.example.model.Analytics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class AnalyticsUsernameValidatorTest {
    private UserRepository userRepository;
    private AnalyticsUsernameValidator usernameValidator;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        usernameValidator = new AnalyticsUsernameValidator("actualUsername", userRepository);
    }

    @Test
    void handle_existingUsername_shouldReturnTrue() {
        Analytics analytics = new Analytics("existingUser");
        when(userRepository.existsById("existingUser")).thenReturn(true);
        boolean result;
        try {
            result = usernameValidator.handle(analytics);
        } catch (InvalidAnalyticsException e) {
            return;
        }
        assertTrue(result);
    }

    @Test
    void handle_nonExistingUsername_shouldThrowInvalidDataException() {
        Analytics analytics = new Analytics("nonExistingUser");
        when(userRepository.existsById("nonExistingUser")).thenReturn(false);
        assertThrows(InvalidDataException.class, () -> usernameValidator.handle(analytics));
    }

    @Test
    void handle_mismatchedUsername_shouldThrowInvalidAnalyticsException() {
        Analytics analytics = new Analytics("mismatchedUser");
        when(userRepository.existsById("mismatchedUser")).thenReturn(true);
        assertThrows(InvalidAnalyticsException.class, () -> usernameValidator.handle(analytics));
    }

    @Test
    void handle_invalidCharactersInUsername_shouldThrowInvalidDataException() {
        Analytics analytics = new Analytics("invalid@User");
        when(userRepository.existsById("invalid@User")).thenReturn(false);
        assertThrows(InvalidDataException.class, () -> usernameValidator.handle(analytics));
    }

    @Test
    void handle_invalidCharactersInUsername_shouldThrowInvalidDataExceptionTwo() {
        Analytics analytics = new Analytics("invalid@User");
        AnalyticsUsernameValidator usernameValidator2 = new AnalyticsUsernameValidator("invalid@User", userRepository);
        when(userRepository.existsById("invalid@User")).thenReturn(true);
        assertThrows(InvalidDataException.class, () -> usernameValidator2.handle(analytics));
    }
}
