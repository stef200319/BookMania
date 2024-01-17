package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.Analytics;
import nl.tudelft.sem.template.example.model.User;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class FollowService {

    private final UserRepository userRepository;
    private final AnalyticsService analyticsService;

    /**
     * Constructs a new UserService with the specified dependencies.
     *
     * @param userRepository The repository for managing user data.
     */
    public FollowService(UserRepository userRepository, AnalyticsService analyticsService) {
        this.userRepository = userRepository;
        this.analyticsService = analyticsService;
    }

    /**
     * Follows another user, updating their relationships and analytics.
     *
     * @param user1 The user initiating the follow.
     * @param user2 The user being followed.
     * @return The user initiating the follow (user1).
     */
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

    /**
     * Unfollows another user, updating their relationships and analytics.
     *
     * @param user1 The user initiating unfollow.
     * @param user2 The user being unfollowed.
     * @return The user initiating unfollow (user1) or null if the unfollow operation fails.
     */
    public User unfollowUser(User user1, User user2) {
        List<String> following = user1.getFollowing();
        if (following == null || !following.contains(user2.getUsername())) {
            return null;
        }
        following.remove(user2.getUsername());

        List<String> followers = user2.getFollowers();
        if (followers == null || !followers.contains(user1.getUsername())) {
            return null;
        }
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
     * Retrieves the list of followers for a given user.
     *
     * <p>
     * This method takes a user object as input, retrieves the list of followers from
     * the user's data, and returns a list of corresponding User objects. If the user has
     * no followers or the follower data is not present, an empty list is returned.
     * </p>
     *
     * @param user The user object for which followers are to be retrieved.
     * @return A list of User objects representing the followers of the specified user,
     *         or an empty list if the user has no followers or the follower data is not present.
     */
    public List<User> getFollowers(User user) {
        List<String> followers = user.getFollowers();

        if (followers == null) {
            return new LinkedList<>();
        }

        List<User> ans = new LinkedList<>();
        for (String i : followers) {
            if (userRepository.existsById(i)) {
                ans.add(userRepository.findById(i).get());
            }
        }
        return ans;
    }

    /**
     * Retrieves the list of users followed by a given user.
     *
     * <p>
     * This method takes a user object as input, retrieves the list of users followed
     * by the user from the user's data, and returns a list of corresponding User objects.
     * If the user is not following anyone or the following data is not present, an empty
     * list is returned.
     * </p>
     *
     * @param user The user object for which the list of followed users is to be retrieved.
     * @return A list of User objects representing the users followed by the specified user,
     *         or an empty list if the user is not following anyone or the following data
     *         is not present.
     */
    public List<User> getFollowing(User user) {
        List<String> following = user.getFollowing();

        if (following == null) {
            return new LinkedList<>();
        }

        List<User> ans = new LinkedList<>();
        for (String i : following) {
            if (userRepository.existsById(i)) {
                ans.add(userRepository.findById(i).get());
            }
        }
        return ans;
    }
}
