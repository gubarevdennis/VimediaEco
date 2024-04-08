package vimedia.service.ReportApp.repo.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.tools.ToolSet;

import java.util.Optional;

public interface ToolSetRepo extends JpaRepository<ToolSet, Integer> {
    Optional<ToolSet> findByName(String name);
}
