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

    public UserInfo deleteUserInfo(String username) {
        UserInfo dbStatus = userInfoRepository.findById(username).get();
        userInfoRepository.deleteById(username);
        return dbStatus;
    }
}
