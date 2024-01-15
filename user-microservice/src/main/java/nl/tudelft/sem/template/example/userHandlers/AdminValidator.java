package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

public class AdminValidator extends BaseUserValidator {
    public AdminValidator(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public boolean handle(User user)
        throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        if(!user.getUserStatus().getUserRole().equals(User.UserRoleEnum.ADMIN))
            throw new InvalidUserException("User is not an admin");

        return super.checkNext(user);
    }
}
