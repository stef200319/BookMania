package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public class PasswordValidator extends BaseUserValidator {
    private final transient User loggingIn;

    public PasswordValidator(UserRepository userRepository, User loggingIn) {
        super(userRepository);
        this.loggingIn = loggingIn;
    }

    @Override
    public boolean handle(User user)
        throws InvalidUsernameException, InvalidUserException, InvalidEmailException {

        if (!user.getUserInfo().getPassword().equals(loggingIn.getUserInfo().getPassword())) {
            throw new InvalidUserException("Wrong password");
        }

        return super.checkNext(user);
    }
}
