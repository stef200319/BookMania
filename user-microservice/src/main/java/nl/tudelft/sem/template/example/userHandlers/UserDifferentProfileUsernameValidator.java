package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public class UserDifferentProfileUsernameValidator extends BaseUserValidator {

    public UserDifferentProfileUsernameValidator(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public boolean handle(User user) throws InvalidUserException, InvalidEmailException, InvalidUsernameException{
        if(!user.getUsername().equals(user.getUserProfile().getUsername())) throw new InvalidUserException("The usernames of the user do not match with the ones of the profile.");
        return super.checkNext(user);
    }
}
