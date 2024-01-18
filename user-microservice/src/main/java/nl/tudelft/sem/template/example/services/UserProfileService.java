package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserProfileRepository;
import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    private final transient UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile createUserProfile(UserProfile userProfile) {
        userProfileRepository.saveAndFlush(userProfile);
        return userProfile;
    }

    /**
     * Edits and updates the user profile for a specific user.
     *
     * <p>
     * This method retrieves the current user profile from the repository using the
     * provided username, applies the changes from the provided edited user profile,
     * and saves the updated information back to the repository. The edited user profile
     * object is then returned.
     * </p>
     *
     * @param userProfile The user profile object containing the changes to be applied.
     * @return The edited user profile object after the changes have been applied and saved.
     */
    public UserProfile editUserProfile(UserProfile userProfile) {
        UserProfile dbStatus = userProfileRepository.findById(userProfile.getUsername()).get();

        dbStatus.setLocation(userProfile.getLocation());
        dbStatus.setProfilePicture(userProfile.getProfilePicture());
        dbStatus.setBio(userProfile.getBio());
        dbStatus.setFavoriteBook(userProfile.getFavoriteBook());
        dbStatus.setFavoriteGenres(userProfile.getFavoriteGenres());

        userProfileRepository.saveAndFlush(dbStatus);
        return userProfile;
    }

    public UserProfile getUserProfile(String username) {
        UserProfile dbStatus = userProfileRepository.findById(username).get();
        return dbStatus;
    }

    /**
     * Deletes the user profile for a specific user based on the provided username.
     *
     * <p>
     * This method retrieves the current user profile from the repository using the
     * provided username, deletes it from the repository, and returns the deleted user
     * profile object.
     * </p>
     *
     * @param username The username of the user whose profile is to be deleted.
     * @return The user profile object that has been deleted.
     */
    public UserProfile deleteUserProfile(String username) {
        UserProfile dbStatus = userProfileRepository.findById(username).get();
        userProfileRepository.deleteById(username);
        return dbStatus;
    }
}
