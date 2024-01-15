package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.userUtilities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
}
