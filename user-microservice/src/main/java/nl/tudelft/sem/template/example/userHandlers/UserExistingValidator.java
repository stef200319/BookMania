package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public class UserExistingValidator extends BaseUserValidator {

    public UserExistingValidator(
        UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public boolean handle(User user)
        throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        if(!userRepository.existsById(user.getUsername()))
            throw new InvalidUserException("User does not exist");

        User userInRepo = userRepository.findById(user.getUsername()).get();

        return super.checkNext(userInRepo);
    }
}
