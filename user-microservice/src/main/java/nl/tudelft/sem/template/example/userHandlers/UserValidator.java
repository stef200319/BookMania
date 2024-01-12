package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public interface UserValidator {
    void setNext(UserValidator handler);

    boolean handle(User user)
        throws InvalidUserException, InvalidUsernameException, InvalidEmailException;
}
