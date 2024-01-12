package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;

public abstract class BaseAnalyticsValidator implements AnalyticsValidator {
    private AnalyticsValidator next;
    protected AnalyticsRepository analyticsRepository;
    public BaseAnalyticsValidator(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }
    public void setNext(AnalyticsValidator handler) {
        this.next = handler;
    }

    public boolean handle(String username) throws IllegalAccessException{
        if(next == null)
            return true;
        return next.handle(username);
    }
}
