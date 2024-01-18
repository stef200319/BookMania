package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private UserRepository userRepository;

    /**
     * Constructs a new UserService with the specified dependencies.
     *
     * @param userRepository The repository for managing user data.
     */
    public SearchService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    /**
     * Finds users based on the provided name and user role criteria.
     *
     * <p>
     * This method constructs an example user object with the specified name and sets the
     * example matching criteria for a case-insensitive, partial matching search in the
     * repository using Spring Data JPA's Example API. The result is then filtered based on
     * the user role and returned as a list.
     * </p>
     *
     * @param name The name to search for in user information (both first name and last name).
     * @param isAuthor A boolean flag indicating whether the users to be retrieved should have
     *                 a specific user role (e.g., author).
     * @return A list of users matching the specified name and user role criteria.
     */
    public List<User> findUsersByName(String name, boolean isAuthor) {
        User exampleUser = new User();
        exampleUser.getUserInfo().setFirstName(name);
        exampleUser.getUserInfo().setLastName(name);
        exampleUser.setUsername(name);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnoreNullValues();

        Example<User> example = Example.of(exampleUser, matcher);

        return userRepository.findAll(example).stream()
            .filter(user -> user.getUserStatus().getUserRole() == exampleUser.getUserStatus().getUserRole())
            .collect(Collectors.toList());
    }

    /**
     * Finds users based on the provided genre and user role criteria.
     *
     * <p>
     * This method constructs an example user object with the specified user role and sets
     * the example matching criteria to ignore null values. The result is then filtered based
     * on the user's favorite genres containing the specified genre and returned as a list.
     * </p>
     *
     * @param genre The genre to search for in user profiles.
     * @param isAuthor A boolean flag indicating whether the users to be retrieved should have
     *                 a specific user role (e.g., author).
     * @return A list of users matching the specified genre and user role criteria.
     */
    public List<User> findUsersByGenre(String genre, boolean isAuthor) {
        User exampleUser = new User();
        exampleUser.getUserStatus().setUserRole(isAuthor ? User.UserRoleEnum.AUTHOR : User.UserRoleEnum.REGULAR);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues();

        Example<User> example = Example.of(exampleUser, matcher);

        return userRepository.findAll(example).stream()
            .filter(user -> user.getUserProfile().getFavoriteGenres().contains(genre))
            .collect(Collectors.toList());
    }

    /**
     * Finds users based on the provided favorite book and user role criteria.
     *
     * <p>
     * This method constructs an example user object with the specified favorite book and
     * user role, and sets the example matching criteria to ignore null values. The result
     * is then returned as a list.
     * </p>
     *
     * @param bookName The name of the favorite book to search for in user profiles.
     * @param isAuthor A boolean flag indicating whether the users to be retrieved should have
     *                 a specific user role (e.g., author).
     * @return A list of users matching the specified favorite book and user role criteria.
     */
    public List<User> findUsersByFavoriteBook(String bookName, boolean isAuthor) {
        User exampleUser = new User();
        exampleUser.getUserProfile().setFavoriteBook(bookName);
        exampleUser.getUserStatus().setUserRole(isAuthor ? User.UserRoleEnum.AUTHOR : User.UserRoleEnum.REGULAR);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues();

        Example<User> example = Example.of(exampleUser, matcher);

        return userRepository.findAll(example);
    }

    /**
     * Finds users based on the provided name and user role criteria, and retrieves users
     * followed by the matched user.
     *
     * <p>
     * This method constructs an example user object with the specified name and sets the
     * example matching criteria for a case-insensitive, partial matching search in the
     * repository using Spring Data JPA's Example API. The result is then filtered based on
     * the user role, and if a matching user is found, retrieves and returns the list of
     * users followed by that user. If no matching user is found, an empty list is returned.
     * </p>
     *
     * @param name The name to search for in user information (both first name and last name).
     * @param isAuthor A boolean flag indicating whether the users to be retrieved should have
     *                 a specific user role (e.g., author).
     * @return A list of users followed by the matched user based on the specified name and
     *         user role criteria, or an empty list if no matching user is found.
     */
    public List<User> findUsersByFollows(String name, boolean isAuthor) {
        User exampleUser = new User();
        exampleUser.getUserInfo().setFirstName(name);
        exampleUser.getUserInfo().setLastName(name);
        exampleUser.setUsername(name);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase()
            .withIgnoreNullValues();


        Example<User> example = Example.of(exampleUser, matcher);
        Optional<User> userOptional = userRepository.findAll(example).stream()
            .filter(user -> user.getUserStatus().getUserRole() == exampleUser.getUserStatus().getUserRole())
            .findFirst();
        if (userOptional.isPresent()) {
            List<String> follow = userOptional.get().getFollowing();
            List<User> ans = new LinkedList<>();
            for (String i : follow) {
                if (userRepository.existsById(i)) {
                    ans.add(userRepository.findById(i).get());
                }
            }
            return ans;
        } else {
            return new ArrayList<>();
        }
    }
}
