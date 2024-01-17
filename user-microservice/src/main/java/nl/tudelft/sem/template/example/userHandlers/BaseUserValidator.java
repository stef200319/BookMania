package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public abstract class BaseUserValidator implements UserValidator {
    private UserValidator next;
    protected UserRepository userRepository;

    public BaseUserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setNext(UserValidator v) {
        this.next = v;
    }

    protected boolean checkNext(User user)
        throws InvalidUserException, InvalidUsernameException, InvalidEmailException {
        if (next == null) {
            return true;
        }

        return next.handle(user);
    }

}
