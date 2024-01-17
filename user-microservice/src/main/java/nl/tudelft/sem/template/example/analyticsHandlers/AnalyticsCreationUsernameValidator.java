package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;

public class AnalyticsCreationUsernameValidator extends BaseAnalyticsValidator {
    private final AnalyticsRepository analyticsRepository;

    /**
     * Creates an instance of this class.
     */
    public AnalyticsCreationUsernameValidator(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    /**
     * Handles the request given as a parameter, in this case checks if a condition for
     * the analytics is met.
     *
     * @param analytics analytics that will be checked
     * @return validation result
     * @throws InvalidAnalyticsException that can be thrown if the analytics entity of this user already exists
     */
    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if (analyticsRepository.findById(analytics.getUserUsername()).isPresent()) {
            throw new InvalidAnalyticsException("The analytics entity of this user already exists.");
        }
        return super.handle(analytics);
    }
}
