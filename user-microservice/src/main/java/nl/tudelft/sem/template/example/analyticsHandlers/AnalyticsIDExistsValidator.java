package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.model.Analytics;

public class AnalyticsIDExistsValidator extends BaseAnalyticsValidator{

    private final AnalyticsRepository analyticsRepository;
    public AnalyticsIDExistsValidator(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if(analyticsRepository.findById(analytics.getUserUsername()).isEmpty()) throw new InvalidAnalyticsException("The database does not contain any entity with this ID.");

        return super.handle(analytics);
    }
}
