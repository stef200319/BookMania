package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.exceptions.InvalidDataException;
import nl.tudelft.sem.template.example.model.Analytics;

public class AnalyticsFollowingValidator extends BaseAnalyticsValidator{
    public AnalyticsFollowingValidator() {
    }

    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if(analytics.getFollowingNumber() < 0) throw new InvalidDataException("The number of following users cannot be lesser than 0.");

        return super.handle(analytics);
    }
}
