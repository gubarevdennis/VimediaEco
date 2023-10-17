package vimedia.service.ReportApp.repo.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.tools.Event;
import vimedia.service.ReportApp.model.tools.Tool;

import java.util.List;
import java.util.Optional;

public interface EventRepo extends JpaRepository<Event, Integer> {
    Optional<Event> findByName(String name);

    List<Event> findByUser(User user);

    List<Event> findByToUser(String toUser);

    List<Event> findByTool(Tool tool);


    @Query(value = "SELECT * FROM EVENT WHERE EVENT.USER_ID =?1 ORDER BY ID DESC \n" +
            "LIMIT 1", nativeQuery = true)
    Event findLastByUser(Integer userID);

    @Query(value = "SELECT * FROM EVENT ORDER BY ID DESC \n" +
            "LIMIT 1", nativeQuery = true)
    Optional<Event> findLastByTool(Tool tool);

    @Query(value = "SELECT * FROM EVENT WHERE EVENT.NAME = 'Направлен на передачу' ORDER BY ID DESC \n" +
            "LIMIT 1", nativeQuery = true)
    Optional<Event> findLastMovingByTool(Tool tool);
}
