package nl.tudelft.sem.template.example.analyticsHandlers;

public interface AnalyticsValidator {
    void setNext(AnalyticsValidator handler);

    boolean handle(String username) throws IllegalAccessException;
}
