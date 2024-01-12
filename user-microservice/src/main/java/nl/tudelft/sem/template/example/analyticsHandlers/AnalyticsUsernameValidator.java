package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.exceptions.InvalidDataException;
import nl.tudelft.sem.template.example.model.Analytics;

public class AnalyticsUsernameValidator extends BaseAnalyticsValidator{
    private final String actualUsername;
    private final UserRepository userRepository;

    public AnalyticsUsernameValidator(String actualUsername, UserRepository userRepository) {
        this.actualUsername = actualUsername;
        this.userRepository = userRepository;
    }

    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if(!userRepository.existsById(analytics.getUserUsername())) throw new InvalidDataException("The username of the edited data does not exist in the database.");

        if(!actualUsername.equals(analytics.getUserUsername())) throw new InvalidAnalyticsException("The username of the analytics entity does not match with the one passed as a parameter.");

        if(!analytics.getUserUsername().matches("^[a-zA-Z][a-zA-Z0-9]*")) throw new InvalidDataException("The username contains illegal characters.");

        return super.handle(analytics);
    }
}
