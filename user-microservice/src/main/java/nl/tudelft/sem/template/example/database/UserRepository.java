package nl.tudelft.sem.template.example.database;

import java.util.Optional;
import nl.tudelft.sem.template.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);
}