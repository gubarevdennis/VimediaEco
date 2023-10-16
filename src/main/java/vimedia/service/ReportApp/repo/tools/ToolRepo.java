package vimedia.service.ReportApp.repo.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.tools.Tool;

import java.util.List;
import java.util.Optional;

public interface ToolRepo extends JpaRepository<Tool, Integer> {
    Optional<Tool> findByName(String name);

    List<Tool> findByUser(User user);

    Optional<Tool> findById(Integer id);
}
