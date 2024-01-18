package nl.tudelft.sem.template.example.services;

import java.util.Optional;
import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnalyticsService {
    private AnalyticsRepository analyticsRepository;

    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public Analytics createAnalytics(Analytics analytics) {
        return analyticsRepository.saveAndFlush(analytics);
    }

    /**
     * Edits and updates the analytics information for a user.
     *
     * <p>
     * This method retrieves the current analytics data for the specified user,
     * applies the changes from the provided edited analytics, and saves the updated
     * information back to the data repository. The updated analytics object is then
     * returned.
     * </p>
     *
     * @param username The username of the user whose analytics are to be edited.
     * @param editedAnalytics The updated analytics object containing the changes to be applied.
     * @return The updated analytics object after the changes have been applied and saved.
     */
    public Analytics editAnalytics(String username, Analytics editedAnalytics) {
        Analytics currentAnalytics = this.analyticsRepository.findById(username).get();

        currentAnalytics.setUserUsername(editedAnalytics.getUserUsername());
        currentAnalytics.setCommentsNumber(editedAnalytics.getCommentsNumber());
        currentAnalytics.setReviewsNumber(editedAnalytics.getReviewsNumber());
        currentAnalytics.setFollowingNumber(editedAnalytics.getFollowingNumber());
        currentAnalytics.setFollowersNumber(editedAnalytics.getFollowersNumber());
        currentAnalytics.setLastLoginDate(editedAnalytics.getLastLoginDate());

        analyticsRepository.saveAndFlush(currentAnalytics);
        return currentAnalytics;
    }

    public Analytics getAnalytics(String username) {
        Optional<Analytics> op = analyticsRepository.findById(username);
        Analytics analytics = op.get();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/analytics/review/count/reviews?countUsers=false&username=" + username;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        long number = Integer.parseInt(response.getBody());
        analytics.setReviewsNumber(number);
        return analytics;
    }

    /**
     * Deletes the analytics information for a user.
     *
     * <p>
     * This method retrieves the current analytics data for the specified user,
     * deletes it from the data repository, and returns the deleted analytics object.
     * </p>
     *
     * @param username The username of the user whose analytics are to be deleted.
     * @return The analytics object that has been deleted.
     */
    public Analytics deleteAnalytics(String username) {
        Analytics deletedAnalytics = this.analyticsRepository.findById(username).get();
        this.analyticsRepository.deleteById(username);
        return deletedAnalytics;
    }
}
