package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserProfileRepository;
import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    private UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile createUserProfile(UserProfile userProfile) {
        userProfileRepository.saveAndFlush(userProfile);
        return userProfile;
    }

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

    public UserProfile deleteUserProfile(String username) {
        UserProfile dbStatus = userProfileRepository.findById(username).get();
        userProfileRepository.deleteById(username);
        return dbStatus;
    }
}
