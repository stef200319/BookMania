package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;

public interface AnalyticsValidator {
    void setNext(AnalyticsValidator handler);

    boolean handle(Analytics analytics) throws InvalidAnalyticsException;
}
