package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.exceptions.InvalidDataException;
import nl.tudelft.sem.template.example.model.Analytics;

public class AnalyticsReviewValidator extends BaseAnalyticsValidator{
    public AnalyticsReviewValidator() {}

    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if(analytics.getReviewsNumber() < 0) throw new InvalidDataException("The number of reviews cannot be lesser than 0.");

        return super.handle(analytics);
    }
}
