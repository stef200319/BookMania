package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User followUser(User user1, User user2) {
        user1.addFollowingItem(user2);
        user2.addFollowersItem(user1);

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);

        return user1;
    }

    public User unfollowUser(User user1, User user2) {
        List<User> following = user1.getFollowing();
        if(following == null || !following.contains(user2))
            return null;
        following.remove(user2);

        List<User> followers = user2.getFollowers();
        if(followers == null || !followers.contains(user1))
            return null;
        followers.remove(user1);

        user1.setFollowing(following);
        user2.setFollowers(followers);

        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);

        return user1;
    }


}
