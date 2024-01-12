package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.AnalyticsRepository;

public class AnalyticsIDExistsValidator extends BaseAnalyticsValidator{
    public AnalyticsIDExistsValidator(AnalyticsRepository analyticsRepository) {
        super(analyticsRepository);
    }

    @Override
    public boolean handle(String username) throws Exception{
        if(analyticsRepository.findById(username).isEmpty()) throw new Exception();

        return super.handle(username);
    }
}
