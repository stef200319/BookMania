package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;

public abstract class BaseAnalyticsValidator implements AnalyticsValidator {
    private AnalyticsValidator next;
    protected AnalyticsRepository analyticsRepository;
    /**
     * Creates an instance of this class
     */
    public BaseAnalyticsValidator() {
        this.next = null;
    }

    /**
     * Sets the next handler in the chain
     *
     * @param handler
     */
    public void setNext(AnalyticsValidator handler) {
        this.next = handler;
    }

    /**
     * Handles the request given as a parameter, in this case checks if a condition for
     * the analytics is met.
     *
     * @param analytics
     * @return
     * @throws InvalidAnalyticsException
     */
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if(next == null)
            return true;
        return next.handle(analytics);
    }
}
