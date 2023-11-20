package vimedia.service.ReportApp.repo.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.tools.InventoryEvent;
import vimedia.service.ReportApp.model.tools.Tool;

import java.util.List;
import java.util.Optional;

public interface InventoryEventRepo extends JpaRepository<InventoryEvent, Integer> {

    List<InventoryEvent> findByUser(User user);

    List<InventoryEvent> findByTool(Tool tool);

    @Query(value = "SELECT * FROM InventoryEvent ORDER BY ID DESC \n" +
            "LIMIT 4", nativeQuery = true)
    Optional<InventoryEvent> findLast4();
}
