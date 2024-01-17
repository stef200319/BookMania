package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.exceptions.InvalidDataException;
import nl.tudelft.sem.template.example.model.Analytics;

public class AnalyticsCommentValidator extends BaseAnalyticsValidator {

    /**
     * Creates an instance of this class.
     */
    public AnalyticsCommentValidator() {
    }

    /**
     * Handles the request given as a parameter, in this case checks if a condition for
     * the analytics is met.
     *
     * @param analytics analytics that will be checked
     * @return validation result
     * @throws InvalidAnalyticsException can be thrown if comment number is less than 0
     */
    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if (analytics.getCommentsNumber() < 0) {
            throw new InvalidDataException("The number of comments cannot be less than 0.");
        }

        return super.handle(analytics);
    }
}
