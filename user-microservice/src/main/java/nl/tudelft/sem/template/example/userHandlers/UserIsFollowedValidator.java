package nl.tudelft.sem.template.example.userHandlers;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;

import java.util.List;

public class UserIsFollowedValidator extends BaseUserValidator {
    private User followedUser;

    public UserIsFollowedValidator(UserRepository userRepository, User followedUser) {
        super(userRepository);
        this.followedUser = followedUser;
    }

    @Override
    public boolean handle(User user)
        throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        List<String> following = user.getFollowing();
        if(following == null || !following.contains(followedUser.getUsername()))
            throw new InvalidUserException("User does not follow other user");

        List<String> followers = followedUser.getFollowers();
        if(followers == null || !followers.contains(user.getUsername()))
            throw new InvalidUserException("User does not follow other user");

        return super.checkNext(user);
    }
}
