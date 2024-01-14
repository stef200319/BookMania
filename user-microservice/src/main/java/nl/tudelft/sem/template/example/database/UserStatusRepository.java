package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, String> {
}
