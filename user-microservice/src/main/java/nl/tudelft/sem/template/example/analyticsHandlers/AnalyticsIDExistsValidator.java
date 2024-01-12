package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;

public class AnalyticsIDExistsValidator extends BaseAnalyticsValidator{
    public AnalyticsIDExistsValidator(AnalyticsRepository analyticsRepository) {
        super(analyticsRepository);
    }

    @Override
    public boolean handle(String username) throws IllegalAccessException{
        if(analyticsRepository.findById(username).isEmpty()) throw new IllegalAccessException();

        return super.handle(username);
    }
}
