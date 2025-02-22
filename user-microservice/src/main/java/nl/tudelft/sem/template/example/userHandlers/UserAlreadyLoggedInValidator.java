package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public class UserAlreadyLoggedInValidator extends BaseUserValidator {
    public UserAlreadyLoggedInValidator(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public boolean handle(User user)
        throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        if (user.getUserStatus().getIsLoggedIn() != null && user.getUserStatus().getIsLoggedIn()) {
            throw new InvalidUserException("User is logged in");
        }

        return super.checkNext(user);
    }
}
