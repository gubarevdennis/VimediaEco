package vimedia.service.ReportApp.repo.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.tools.Category;
import vimedia.service.ReportApp.model.tools.ToolSet;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}
