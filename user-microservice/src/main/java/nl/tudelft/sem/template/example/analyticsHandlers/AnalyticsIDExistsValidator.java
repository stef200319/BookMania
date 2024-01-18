package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;

public class AnalyticsIDExistsValidator extends BaseAnalyticsValidator {

    private transient final AnalyticsRepository analyticsRepository;
    /**
     * Creates an instance of this class.
     */

    public AnalyticsIDExistsValidator(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    /**
     * Handles the request given as a parameter, in this case checks if a condition for
     * the analytics is met.
     *
     * @param analytics that will be checked
     * @return validation result
     * @throws InvalidAnalyticsException can be thrown if the database does not contain any entity with this ID
     */
    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if (!analyticsRepository.existsById(analytics.getUserUsername())) {
            throw new InvalidAnalyticsException("The database does not contain any entity with this ID.");
        }

        return super.handle(analytics);
    }
}
