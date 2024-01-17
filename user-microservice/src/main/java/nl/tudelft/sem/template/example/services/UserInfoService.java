package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserInfoRepository;
import nl.tudelft.sem.template.example.database.UserStatusRepository;
import nl.tudelft.sem.template.example.userUtilities.UserInfo;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    private UserInfoRepository userInfoRepository;

    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public UserInfo createUserInfo(UserInfo userInfo) {
        userInfoRepository.saveAndFlush(userInfo);
        return userInfo;
    }

    /**
     * Edits and updates the user information for a specific user.
     *
     * <p>
     * This method retrieves the current user information from the repository using the
     * provided username, applies the changes from the provided edited user information,
     * and saves the updated information back to the repository. The edited user information
     * object is then returned.
     * </p>
     *
     * @param editUserInfo The user information object containing the changes to be applied.
     * @return The edited user information object after the changes have been applied and saved.
     */
    public UserInfo editUserInfo(UserInfo editUserInfo) {
        UserInfo dbStatus = userInfoRepository.findById(editUserInfo.getUsername()).get();

        dbStatus.setPassword(editUserInfo.getPassword());
        dbStatus.setEmail(editUserInfo.getEmail());
        dbStatus.setFirstName(editUserInfo.getFirstName());
        dbStatus.setLastName(editUserInfo.getLastName());

        userInfoRepository.saveAndFlush(dbStatus);
        return editUserInfo;
    }

    public UserInfo getUserInfo(String username) {
        UserInfo dbStatus = userInfoRepository.findById(username).get();
        return dbStatus;
    }

    /**
     * Deletes the user information for a specific user based on the provided username.
     *
     * <p>
     * This method retrieves the current user information from the repository using the
     * provided username, deletes it from the repository, and returns the deleted user
     * information object.
     * </p>
     *
     * @param username The username of the user whose information is to be deleted.
     * @return The user information object that has been deleted.
     */
    public UserInfo deleteUserInfo(String username) {
        UserInfo dbStatus = userInfoRepository.findById(username).get();
        userInfoRepository.deleteById(username);
        return dbStatus;
    }
}
