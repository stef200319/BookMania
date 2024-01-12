package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public class UserAlreadyActiveValidator extends BaseUserValidator {
    public UserAlreadyActiveValidator(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public boolean handle(User user)
        throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        if(user.getIsActive())
            throw new InvalidUserException("User is active");

        return super.checkNext(user);
    }
}