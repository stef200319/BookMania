package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserStatusRepository;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.UnicodeEscaper;

@Service
public class UserStatusService {

    private UserStatusRepository userStatusRepository;

    public UserStatusService(UserStatusRepository userStatusRepository) {
        this.userStatusRepository = userStatusRepository;
    }

    public UserStatus createUserStatus(UserStatus userStatus) {
        userStatusRepository.saveAndFlush(userStatus);
        return userStatus;
    }

    /**
     * Edits the user status of a specific user based on the provided updated user status.
     *
     * <p>
     * This method retrieves the current user status from the repository using the
     * username from the updated user status. It then updates the user role, login status,
     * ban status, and activation status of the user status with the values from the provided
     * updated user status. The changes are persisted to the repository, and the updated user
     * status object is returned.
     * </p>
     *
     * @param editUserstatus The updated user status object containing the modified user status details.
     * @return The updated user status object after the changes have been persisted to the repository.
     */
    public UserStatus editUserStatus(UserStatus editUserstatus) {
        UserStatus dbStatus = userStatusRepository.findById(editUserstatus.getUsername()).get();

        dbStatus.setUserRole(editUserstatus.getUserRole());
        dbStatus.setIsLoggedIn(editUserstatus.getIsLoggedIn());
        dbStatus.setIsBanned(editUserstatus.getIsBanned());
        dbStatus.setIsActive(editUserstatus.getIsActive());

        userStatusRepository.saveAndFlush(dbStatus);
        return editUserstatus;
    }

    public UserStatus getUserStatus(String username) {
        UserStatus dbStatus = userStatusRepository.findById(username).get();
        return dbStatus;
    }

    /**
     * Deletes the user status of a specific user based on the provided username.
     *
     * <p>
     * This method fetches the user status from the repository using the provided username,
     * deletes the user status from the repository, and returns the deleted user status object.
     * If the user status with the specified username is not found in the repository, the method
     * takes no action, and a NoSuchElementException may be thrown.
     * </p>
     *
     * @param username The username of the user whose user status is to be deleted.
     * @return The deleted user status object, or null if the user status with the specified username
     *         is not found in the repository.
     */
    public UserStatus deleteUserStatus(String username) {
        UserStatus dbStatus = userStatusRepository.findById(username).get();
        userStatusRepository.deleteById(username);
        return dbStatus;
    }
}

