package nl.tudelft.sem.template.example.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.userUtilities.UserInfo;
import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private AnalyticsService analyticsService;
    private UserStatusService userStatusService;
    private UserProfileService userProfileService;
    private UserInfoService userInfoService;

    /**
     * Constructs a new UserService with the specified dependencies.
     *
     * @param userRepository The repository for managing user data.
     * @param analyticsService The service for managing user analytics data.
     * @param userStatusService The service for managing user status data.
     * @param userProfileService The service for managing user profile data.
     * @param userInfoService The service for managing user information data.
     */
    public UserService(UserRepository userRepository, AnalyticsService analyticsService,
                       UserStatusService userStatusService, UserProfileService userProfileService,
                            UserInfoService userInfoService) {
        this.userRepository = userRepository;
        this.analyticsService = analyticsService;
        this.userStatusService = userStatusService;
        this.userProfileService = userProfileService;
        this.userInfoService = userInfoService;
    }

    /**
     * Logs in a user by updating their status, saving analytics, and returning the user object.
     *
     * @param user1 The user to log in.
     * @return The logged-in user object.
     */
    public User logInUser(User user1) {
        user1.getUserStatus().setIsLoggedIn(true);
        userRepository.saveAndFlush(user1);
        userStatusService.editUserStatus(user1.getUserStatus());
        Analytics analytics = analyticsService.getAnalytics(user1.getUsername());
        analytics.setLastLoginDate(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()));
        analyticsService.editAnalytics(user1.getUsername(), analytics);
        return user1;
    }

    /**
     * Logs out a user by updating their status and saving the changes.
     *
     * @param user1 The user to log out.
     * @return The logged-out user object.
     */
    public User logOutUser(User user1) {
        user1.getUserStatus().setIsLoggedIn(false);
        userRepository.saveAndFlush(user1);
        userStatusService.editUserStatus(user1.getUserStatus());
        userRepository.saveAndFlush(user1);
        return user1;
    }

    /**
     * Updates the personal information of a user based on the provided modified user object.
     *
     * <p>
     * This method retrieves the current user from the repository using the username from
     * the modified user object. It then performs modifications to the personal information,
     * including user profile and user information, and saves the changes to the repository.
     * </p>
     *
     * @param modifiedUser The modified user object containing the updated personal information.
     * @return The user object with the updated personal information.
     */
    public User updateUserInfo(User modifiedUser) {
        User currentUser = userRepository.findById(modifiedUser.getUsername()).get();
        // Perform modifications of the personal info of the user
        currentUser.getUserProfile().setBio(modifiedUser.getUserProfile().getBio());
        currentUser.getUserProfile().setProfilePicture(modifiedUser.getUserProfile().getProfilePicture());
        currentUser.getUserProfile().setLocation(modifiedUser.getUserProfile().getLocation());
        currentUser.getUserProfile().setFavoriteBook(modifiedUser.getUserProfile().getFavoriteBook());
        currentUser.getUserProfile().setFavoriteGenres(modifiedUser.getUserProfile().getFavoriteGenres());
        UserProfile profile = currentUser.getUserProfile();
        userProfileService.editUserProfile(profile);

        currentUser.getUserInfo().setFirstName(modifiedUser.getUserInfo().getFirstName());
        currentUser.getUserInfo().setLastName(modifiedUser.getUserInfo().getLastName());
        currentUser.getUserInfo().setPassword(modifiedUser.getUserInfo().getPassword());
        currentUser.getUserInfo().setEmail(modifiedUser.getUserInfo().getEmail());
        UserInfo info = currentUser.getUserInfo();
        userInfoService.editUserInfo(info);

        userRepository.saveAndFlush(currentUser);
        return currentUser;
    }

    /**
     * Modifies the activation status of a user based on the provided parameters.
     *
     * <p>
     * This method retrieves the current user from the repository using the provided
     * username. It then modifies the activation status of the user according to the
     * specified flag. The changes are persisted to the repository, and the modified
     * user object is returned.
     * </p>
     *
     * @param currentUsername The username of the user whose activation status is to be modified.
     * @param flag A boolean flag indicating the desired activation status (true for active, false for inactive).
     * @return The user object with the modified activation status.
     */
    public User modifyUserActivationStatus(String currentUsername, boolean flag) {
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
     * Fetches a specific User instance from the database based on the provided username.
     *
     * @param username The username of the desired User instance.
     * @return The User instance corresponding to the provided username.
     */
    public User fetchUser(String username) {
        return userRepository.findById(username).get();
    }

    /**
     * Deletes a user and associated data from the database based on the provided username.
     *
     * <p>
     * This method first fetches the user from the repository using the provided username.
     * It then iterates over the user's following and follower lists, updating the respective
     * lists of other users to remove references to the deleted user. After that, it deletes
     * the user from the repository and invokes services to delete associated user status,
     * user profile, and user information. If the user is not found in the repository, the
     * method takes no action.
     * </p>
     *
     * @param username The username of the user to be deleted.
     */
    public void deleteUser(String username) {
        User user = userRepository.findById(username).get();

        //deleting user from following / follower lists
        List<String> following = user.getFollowing();

        if (following != null) {
            for (String i : following) {
                if (userRepository.existsById(i)) {
                    User u = userRepository.findById(i).get();
                    List<String> list = u.getFollowers();
                    list.remove(username);
                    u.setFollowers(list);
                    userRepository.saveAndFlush(u);
                }
            }
        }

        List<String> followers = user.getFollowers();

        if (followers != null) {
            for (String i : followers) {
                if (userRepository.existsById(i)) {
                    User u = userRepository.findById(i).get();
                    List<String> list = u.getFollowing();
                    list.remove(username);
                    u.setFollowing(list);
                    userRepository.saveAndFlush(u);
                }
            }
        }

        userRepository.deleteById(username);
        analyticsService.deleteAnalytics(username);
        userStatusService.deleteUserStatus(username);
        userProfileService.deleteUserProfile(username);
        userInfoService.deleteUserInfo(username);
    }

    public void deleteFollowing(User user) {

    }

    /**
     * Creates a new user in the system, including associated user profile, user status,
     * user information, and analytics.
     *
     * <p>
     * This method takes a user object and invokes corresponding services to create user
     * profile, user status, and user information in the system. It then saves the user to
     * the repository and creates an analytics entry for the user. The saved user object,
     * including any generated identifiers, is returned.
     * </p>
     *
     * @param user The user object containing the details of the user to be created.
     * @return The saved user object with associated data created in the system.
     */
    public User createUser(User user) {
        userProfileService.createUserProfile(user.getUserProfile());
        userStatusService.createUserStatus(user.getUserStatus());
        userInfoService.createUserInfo(user.getUserInfo());
        User saved = userRepository.saveAndFlush(user);
        Analytics analytics = new Analytics(saved.getUsername());
        analyticsService.createAnalytics(analytics);
        return saved;
    }

}
