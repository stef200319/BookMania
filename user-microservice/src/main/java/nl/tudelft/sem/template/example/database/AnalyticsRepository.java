package nl.tudelft.sem.template.example.database;

import nl.tudelft.sem.template.example.model.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, String> {
}
