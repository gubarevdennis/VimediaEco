package vimedia.service.ReportApp.repo.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.tools.Event;

import java.util.Optional;

public interface EventRepo extends JpaRepository<Event, Integer> {
    Optional<Event> findByName(String name);
}
