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

    public UserStatus deleteUserStatus(String username) {
        UserStatus dbStatus = userStatusRepository.findById(username).get();
        userStatusRepository.deleteById(username);
        return dbStatus;
    }
}

