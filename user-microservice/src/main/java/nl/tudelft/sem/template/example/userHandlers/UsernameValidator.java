package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public class UsernameValidator extends  BaseUserValidator {

    public UsernameValidator(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public boolean handle(User user)
        throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        String username = user.getUsername();

        if (userRepository.existsById(username)) {
            throw new InvalidUsernameException("Username already in use");
        }

        if (!username.matches("^[a-zA-Z][a-zA-Z0-9]*")) {
            throw new InvalidUsernameException("Invalid username format");
        }

        return super.checkNext(user);
    }
}
