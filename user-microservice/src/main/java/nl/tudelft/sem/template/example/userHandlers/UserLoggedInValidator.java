package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public class UserLoggedInValidator extends BaseUserValidator {
    public UserLoggedInValidator(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public boolean handle(User user)
        throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        if(user.getUserStatus().getIsLoggedIn() == null || !user.getUserStatus().getIsLoggedIn())
            throw new InvalidUserException("User is not logged in");

        return super.checkNext(user);
    }
}
