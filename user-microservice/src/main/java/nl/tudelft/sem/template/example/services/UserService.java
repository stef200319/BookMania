package nl.tudelft.sem.template.example.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private AnalyticsService analyticsService;

    private UserStatusService userStatusService;

    private UserProfileService userProfileService;

    public UserService(UserRepository userRepository, AnalyticsService analyticsService, UserStatusService userStatusService, UserProfileService userProfileService) {
        this.userRepository = userRepository;
        this.analyticsService = analyticsService;
        this.userStatusService = userStatusService;
        this.userProfileService = userProfileService;
    }

    public User logInUser(User user1) {
        user1.getUserStatus().setIsLoggedIn(true);
        userRepository.saveAndFlush(user1);
        userStatusService.editUserStatus(user1.getUserStatus());
        Analytics analytics = analyticsService.getAnalytics(user1.getUsername());
        analytics.setLastLoginDate(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()));
        analyticsService.editAnalytics(user1.getUsername(), analytics);
        return user1;
    }

    public User logOutUser(User user1) {
        user1.getUserStatus().setIsLoggedIn(false);
        userRepository.saveAndFlush(user1);
        userStatusService.editUserStatus(user1.getUserStatus());
        userRepository.saveAndFlush(user1);
        return user1;
    }

    public User followUser(User user1, User user2) {
        user1.addFollowingItem(user2);
        user2.addFollowersItem(user1);

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);

        Analytics analytics1 = analyticsService.getAnalytics(user1.getUsername());
        Analytics analytics2 = analyticsService.getAnalytics(user2.getUsername());

        analytics1.setFollowingNumber(analytics1.getFollowingNumber() + 1);
        analytics2.setFollowersNumber(analytics2.getFollowersNumber() + 1);
        analyticsService.editAnalytics(user1.getUsername(), analytics1);
        analyticsService.editAnalytics(user2.getUsername(), analytics2);

        return user1;
    }

    public User unfollowUser(User user1, User user2) {
        List<String> following = user1.getFollowing();
        if(following == null || !following.contains(user2.getUsername()))
            return null;
        following.remove(user2.getUsername());

        List<String> followers = user2.getFollowers();
        if(followers == null || !followers.contains(user1.getUsername()))
            return null;
        followers.remove(user1.getUsername());

        user1.setFollowing(following);
        user2.setFollowers(followers);

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);

        Analytics analytics1 = analyticsService.getAnalytics(user1.getUsername());
        Analytics analytics2 = analyticsService.getAnalytics(user2.getUsername());

        analytics1.setFollowingNumber(analytics1.getFollowingNumber() - 1);
        analytics2.setFollowersNumber(analytics2.getFollowersNumber() - 1);
        analyticsService.editAnalytics(user1.getUsername(), analytics1);
        analyticsService.editAnalytics(user2.getUsername(), analytics2);

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
            .filter(user -> user.getUserStatus().getUserRole() == exampleUser.getUserStatus().getUserRole())
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
        exampleUser.getUserStatus().setUserRole(isAuthor ? User.UserRoleEnum.AUTHOR : User.UserRoleEnum.REGULAR);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues();

        Example<User> example = Example.of(exampleUser, matcher);

        return userRepository.findAll(example).stream()
            .filter(user -> user.getUserProfile().getFavoriteGenres().contains(genre))
            .collect(Collectors.toList());
    }

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
            .filter(user -> user.getUserStatus().getUserRole() == exampleUser.getUserStatus().getUserRole())
            .findFirst();
        if(userOptional.isPresent()) {
            List<String> follow = userOptional.get().getFollowing();
            List<User> ans = new LinkedList<>();
            for(String i : follow) {
                if(userRepository.existsById(i))
                    ans.add(userRepository.findById(i).get());
            }
            return ans;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Update the values of the fields of an existing User instance
     * @param modifiedUser - the modified corresponding User instance
     * @return - the updated User instance that is persisted in the DB
     */
    public User updateUserInfo(User modifiedUser){
        User currentUser = userRepository.findById(modifiedUser.getUsername()).get();
        // Perform modifications of the personal info of the user
        currentUser.getUserProfile().setBio(modifiedUser.getUserProfile().getBio());
        currentUser.getUserProfile().setProfilePicture(modifiedUser.getUserProfile().getProfilePicture());
        currentUser.getUserProfile().setLocation(modifiedUser.getUserProfile().getLocation());
        currentUser.getUserProfile().setFavoriteBook(modifiedUser.getUserProfile().getFavoriteBook());
        currentUser.getUserProfile().setFavoriteGenres(modifiedUser.getUserProfile().getFavoriteGenres());
        UserProfile profile = currentUser.getUserProfile();
        userProfileService.editUserProfile(profile);

        currentUser.setFirstName(modifiedUser.getFirstName());
        currentUser.setLastName(modifiedUser.getLastName());
        currentUser.setPassword(modifiedUser.getPassword());
        currentUser.setEmail(modifiedUser.getEmail());
        userRepository.saveAndFlush(currentUser);
        return currentUser;
    }

    /**
     * Modify the activation status of a User instance
     * @param currentUsername - the username corresponding to the modified User instance
     * @param flag - new value of the activation status
     * @return - the modified User instance
     */
    public User modifyUserActivationStatus(String currentUsername, boolean flag){
        // Fetch the User instance from the DB
        User currentUser = userRepository.findById(currentUsername).get();
        // Modify the activation status of the user
        currentUser.getUserStatus().setIsActive(flag);
        UserStatus status = currentUser.getUserStatus();
        userStatusService.editUserStatus(status);
        // Persist the changes to the DB
        userRepository.saveAndFlush(currentUser);
        return currentUser;
    }

    /**
     * Fetch a specific User instance from the DB
     * @param username - the username of the desired User instance
     * @return - the desired User instance
     */
    public User fetchUser(String username){
        return userRepository.findById(username).get();
    }

    /**
     * Delete a User instance with the respective username
     * @param username - the username of the User instance to be deleted
     */
    public void deleteUser(String username){
        User user = userRepository.findById(username).get();

        //deleting user from following / follower lists
        List<String> following = user.getFollowing();

        if(following!=null) {
            for(String i : following) {
                if(userRepository.existsById(i)) {
                    User u = userRepository.findById(i).get();
                    List<String> list = u.getFollowers();
                    list.remove(username);
                    u.setFollowers(list);
                    userRepository.saveAndFlush(u);
                }
            }
        }

        List<String> followers = user.getFollowers();

        if(followers!=null) {
            for(String i : followers) {
                if(userRepository.existsById(i)) {
                    User u = userRepository.findById(i).get();
                    List<String> list = u.getFollowing();
                    list.remove(username);
                    u.setFollowing(list);
                    userRepository.saveAndFlush(u);
                }
            }
        }

        userRepository.deleteById(username);
        userStatusService.deleteUserStatus(username);
        userProfileService.deleteUserProfile(username);
    }

    public User createUser(User user) {
        userProfileService.createUserProfile(user.getUserProfile());
        userStatusService.createUserStatus(user.getUserStatus());
        User saved = userRepository.saveAndFlush(user);
        Analytics analytics = new Analytics(saved.getUsername());
        analyticsService.createAnalytics(analytics);
        return saved;
    }

    public List<User> getFollowers(User user) {
        List<String> followers = user.getFollowers();

        if(followers == null)
            return new LinkedList<User>();

        List<User> ans = new LinkedList<>();
        for(String i : followers) {
            if(userRepository.existsById(i))
                ans.add(userRepository.findById(i).get());
        }
        return ans;
    }

    public List<User> getFollowing(User user) {
        List<String> following = user.getFollowing();

        if(following == null)
            return new LinkedList<User>();

        List<User> ans = new LinkedList<>();
        for(String i : following) {
            if(userRepository.existsById(i))
                ans.add(userRepository.findById(i).get());
        }
        return ans;
    }

}
