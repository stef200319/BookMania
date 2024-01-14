package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
