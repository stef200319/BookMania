package nl.tudelft.sem.template.example.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User logInUser(User user1) {
        user1.setIsLoggedIn(true);
        userRepository.saveAndFlush(user1);
        return user1;
    }

    public User logOutUser(User user1) {
        user1.setIsLoggedIn(false);
        userRepository.saveAndFlush(user1);
        return user1;
    }

    public User followUser(User user1, User user2) {
        user1.addFollowingItem(user2);
        user2.addFollowersItem(user1);

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);

        return user1;
    }

    public User unfollowUser(User user1, User user2) {
        List<User> following = user1.getFollowing();
        if(following == null || !following.contains(user2))
            return null;
        following.remove(user2);

        List<User> followers = user2.getFollowers();
        if(followers == null || !followers.contains(user1))
            return null;
        followers.remove(user1);

        user1.setFollowing(following);
        user2.setFollowers(followers);

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);

        return user1;
    }

    /**
     * Returns all users with either a username, first name or last name that matches the query
     * @param name Name to match.
     * @param isAuthor Whether the user should be an author.
     * @return List of users with a matching name.
     */
    public List<User> findUsersByName(String name, boolean isAuthor) {
        User exampleUser = new User();
        exampleUser.setFirstName(name);
        exampleUser.setLastName(name);
        exampleUser.setUsername(name);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnoreNullValues();

        Example<User> example = Example.of(exampleUser, matcher);

        return userRepository.findAll(example).stream()
            .filter(user -> user.getUserRole() == exampleUser.getUserRole())
            .collect(Collectors.toList());
    }

    /**
     * Returns all users that have the genre in their favorite genre lists
     * @param genre The genre to search with
     * @param isAuthor Whether the user should be an author
     * @return List of users that like the genre
     */
    public List<User> findUsersByGenre(String genre, boolean isAuthor) {
        User exampleUser = new User();
        exampleUser.setUserRole(isAuthor ? User.UserRoleEnum.AUTHOR : User.UserRoleEnum.REGULAR);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues();

        Example<User> example = Example.of(exampleUser, matcher);

        return userRepository.findAll(example).stream()
            .filter(user -> user.getFavoriteGenres().contains(genre))
            .collect(Collectors.toList());
    }

    public List<User> findUsersByFavoriteBook(String bookName, boolean isAuthor) {
        User exampleUser = new User();
        exampleUser.setFavoriteBook(bookName);
        exampleUser.setUserRole(isAuthor ? User.UserRoleEnum.AUTHOR : User.UserRoleEnum.REGULAR);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues();

        Example<User> example = Example.of(exampleUser, matcher);

        return userRepository.findAll(example);
    }

    /**
     * Finds all followers of a user with the given name.
     * @param name The user to find.
     * @param isAuthor Whether the user is an author.
     * @return The followers of a user.
     */
    public List<User> findUsersByFollows(String name, boolean isAuthor) {
        User exampleUser = new User();
        exampleUser.setFirstName(name);
        exampleUser.setLastName(name);
        exampleUser.setUsername(name);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnoreNullValues();


        Example<User> example = Example.of(exampleUser, matcher);
        Optional<User> userOptional = userRepository.findAll(example).stream()
            .filter(user -> user.getUserRole() == exampleUser.getUserRole())
            .findFirst();
        if(userOptional.isPresent()) {
            return userOptional.get().getFollowing();
        } else {
            return new ArrayList<>();
        }
    }


}
