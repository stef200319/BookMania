package nl.tudelft.sem.template.example.handlers.analyticsHandlers;

import nl.tudelft.sem.template.api.AnalyticsApi;

public abstract class BaseAnalyticsValidator implements AnalyticsValidator {
    private AnalyticsValidator next;

    public void setNext(AnalyticsValidator handler) {
        this.next = handler;
    }

    protected boolean checkNext() {
        return false;
    }
}
