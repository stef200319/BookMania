package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;

public abstract class BaseAnalyticsValidator implements AnalyticsValidator {
    private transient AnalyticsValidator next;

    /**
     * Creates an instance of this class.
     */
    public BaseAnalyticsValidator() {}

    /**
     * Sets the next handler in the chain.
     *
     * @param handler validator
     */
    public void setNext(AnalyticsValidator handler) {
        this.next = handler;
    }

    /**
     * Handles the request given as a parameter, in this case checks if a condition for
     * the analytics is met.
     *
     * @param analytics that will be checked
     * @return validation result
     * @throws InvalidAnalyticsException can be thrown if the validator is null
     */
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if (next == null) {
            return true;
        }

        return next.handle(analytics);
    }
}
