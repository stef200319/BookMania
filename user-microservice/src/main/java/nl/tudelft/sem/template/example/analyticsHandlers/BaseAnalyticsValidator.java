package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;

public abstract class BaseAnalyticsValidator implements AnalyticsValidator {
    private AnalyticsValidator next;
    protected AnalyticsRepository analyticsRepository;
    public BaseAnalyticsValidator(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }
    public void setNext(AnalyticsValidator handler) {
        this.next = handler;
    }

    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if(next == null)
            return true;
        return next.handle(analytics);
    }
}
