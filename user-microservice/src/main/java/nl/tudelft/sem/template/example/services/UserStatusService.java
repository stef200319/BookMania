package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserStatusRepository;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.UnicodeEscaper;

@Service
public class UserStatusService {

    private UserStatusRepository userStatusRepository;
    public UserStatus createUserStatus(UserStatus userStatus) {
        userStatusRepository.saveAndFlush(userStatus);
        return userStatus;
    }

    public UserStatus editUserStatus(String username, UserStatus editUserstatus) {

    }

    public UserStatus getUserStatus(String username) {

    }
}

