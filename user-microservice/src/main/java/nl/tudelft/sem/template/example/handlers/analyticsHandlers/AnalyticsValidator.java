package nl.tudelft.sem.template.example.handlers.analyticsHandlers;

import nl.tudelft.sem.template.example.model.Analytics;

public interface AnalyticsValidator {
    void setNext(AnalyticsValidator handler);

    boolean handle(Analytics analytics);
}
