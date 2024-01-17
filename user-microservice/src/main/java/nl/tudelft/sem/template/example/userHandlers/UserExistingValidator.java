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
        String username = user.getUsername();


        if (!userRepository.existsById(username)) {
            throw new InvalidUserException("User does not exist");
        }

        User userInRepo = userRepository.findById(username).get();

        return super.checkNext(userInRepo);
    }
}
