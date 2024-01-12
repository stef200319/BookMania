package nl.tudelft.sem.template.example.database;

import java.util.List;
import nl.tudelft.sem.template.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);
}