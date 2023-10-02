package vimedia.service.ReportApp.repo.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.tools.Tool;

import java.util.Optional;

public interface ToolRepo extends JpaRepository<Tool, Integer> {
    Optional<Tool> findByName(String name);
}
