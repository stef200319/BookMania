package nl.tudelft.sem.template.example.service;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class AnalyticsService {
    private AnalyticsRepository analyticsRepository;

    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public Analytics createAnalytics(Analytics analytics) {
        return analyticsRepository.saveAndFlush(analytics);
    }

    public Analytics editAnalytics(String username, Analytics editedAnalytics) {
        Analytics currentAnalytics = this.analyticsRepository.findById(username).get();

        currentAnalytics.setUserUsername(editedAnalytics.getUserUsername());
        currentAnalytics.setCommentsNumber(editedAnalytics.getCommentsNumber());
        currentAnalytics.setReviewsNumber(editedAnalytics.getReviewsNumber());
        currentAnalytics.setFollowingNumber(editedAnalytics.getFollowingNumber());
        currentAnalytics.setFollowersNumber(editedAnalytics.getFollowersNumber());
        currentAnalytics.setLastLoginDate(editedAnalytics.getLastLoginDate());
        return analyticsRepository.saveAndFlush(currentAnalytics);
    }

    public Analytics getAnalytics(String username) {
        Optional<Analytics> op = analyticsRepository.findById(username);
        return op.get();
    }

    public Analytics deleteAnalytics(String username) {
        Analytics deletedAnalytics = this.analyticsRepository.findById(username).get();
        this.analyticsRepository.deleteById(username);
        return deletedAnalytics;
    }
}
