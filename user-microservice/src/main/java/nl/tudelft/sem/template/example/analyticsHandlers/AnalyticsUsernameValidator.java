package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.exceptions.InvalidDataException;
import nl.tudelft.sem.template.example.model.Analytics;

public class AnalyticsUsernameValidator extends BaseAnalyticsValidator {
    private transient final String actualUsername;
    private transient final UserRepository userRepository;

    /**
     * Creates an instance of this class.
     */
    public AnalyticsUsernameValidator(String actualUsername, UserRepository userRepository) {
        this.actualUsername = actualUsername;
        this.userRepository = userRepository;
    }

    /**
     * Handles the request given as a parameter, in this case checks if a condition for
     * the analytics is met.
     *
     * @param analytics that will be checked
     * @return validation result
     * @throws InvalidDataException can be thrown if the username of the edited data does not exist in the database,
     *      or the username contains illegal characters
     * @throws InvalidAnalyticsException if the username of the analytics entity does not match
     *      with the one passed as a parameter
     */
    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        if (!userRepository.existsById(analytics.getUserUsername())) {
            throw new InvalidDataException("The username of the edited data does not exist in the database.");
        }

        if (!actualUsername.equals(analytics.getUserUsername())) {
            throw new InvalidAnalyticsException("The username of the analytics entity "
                    + "does not match with the one passed as a parameter.");
        }

        if (!analytics.getUserUsername().matches("^[a-zA-Z][a-zA-Z0-9]*")) {
            throw new InvalidDataException("The username contains illegal characters.");
        }

        return super.handle(analytics);
    }
}
