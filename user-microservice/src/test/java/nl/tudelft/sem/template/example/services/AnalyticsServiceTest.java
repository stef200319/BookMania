package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyticsServiceTest {
    private AnalyticsRepository analyticsRepository;
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        analyticsRepository = Mockito.mock(AnalyticsRepository.class);
        analyticsService = new AnalyticsService(analyticsRepository);
    }

    @Test
    void createAnalyticsTest() {
        Analytics a = new Analytics("test", "now");

        Mockito.when(analyticsRepository.saveAndFlush(a)).thenReturn(a);

        Analytics result = analyticsService.createAnalytics(a);

        assertEquals(result, a);
        Mockito.verify(analyticsRepository, Mockito.times(1)).saveAndFlush(a);
    }

    @Test
    void editAnalyticsTest() {
        Analytics a = new Analytics("test", "now");
        Analytics b = new Analytics("bega", "now");

        Mockito.when(analyticsRepository.findById("test")).thenReturn(Optional.of(a));
        Mockito.when(analyticsRepository.saveAndFlush(a)).thenReturn(b);

        Analytics result = analyticsService.editAnalytics("test", b);

        assertEquals(result, b);
        Mockito.verify(analyticsRepository, Mockito.times(1)).saveAndFlush(b);
        Mockito.verify(analyticsRepository, Mockito.times(1)).findById("test");
    }

    @Test
    void editAnalyticsTestBetter() {
        String username = "user123";
        Analytics currentAnalytics = new Analytics(username, 5L, 3L, "2021-12-01", 10L, 20L);
        Analytics editAnalytics = new Analytics(username, 7L, 10L, "2021-12-03", 50L, 2L);

        Mockito.when(analyticsRepository.findById("user123")).thenReturn(Optional.of(currentAnalytics));
        Mockito.when(analyticsRepository.saveAndFlush(currentAnalytics)).thenReturn(editAnalytics);

        Analytics result = analyticsService.editAnalytics("user123", editAnalytics);

        assertEquals(result, editAnalytics);
        Mockito.verify(analyticsRepository, Mockito.times(1)).saveAndFlush(editAnalytics);
        Mockito.verify(analyticsRepository, Mockito.times(1)).findById("user123");
    }

    @Test
    void getAnalyticsTest() {
        Analytics a = new Analytics("test", "now");
        Mockito.when(analyticsRepository.findById("test")).thenReturn(Optional.of(a));

        Analytics result = analyticsService.getAnalytics("test");

        assertEquals(result, a);

        Mockito.verify(analyticsRepository, Mockito.times(1)).findById("test");
    }

    @Test
    void deleteAnalyticsTest() {
        Analytics a = new Analytics("test", "now");
        Mockito.when(analyticsRepository.findById("test")).thenReturn(Optional.of(a));
        Mockito.doNothing().when(analyticsRepository).deleteById("test");

        Analytics result = analyticsService.deleteAnalytics("test");

        assertEquals(result, a);

        Mockito.verify(analyticsRepository, Mockito.times(1)).findById("test");
        Mockito.verify(analyticsRepository, Mockito.times(1)).deleteById("test");
    }
}
