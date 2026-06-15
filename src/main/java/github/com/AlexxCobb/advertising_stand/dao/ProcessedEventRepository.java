package github.com.AlexxCobb.advertising_stand.dao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Integer> {

    boolean existsByEventId(String eventId);
}
