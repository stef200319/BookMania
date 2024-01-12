package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;

public class AnalyticsCreationUsernameValidator extends BaseAnalyticsValidator{
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsCreationUsernameValidator(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if(analyticsRepository.findById(analytics.getUserUsername()).isPresent()) throw new InvalidAnalyticsException("The analytics entity of this user already exists.");
        return super.handle(analytics);
    }
}
