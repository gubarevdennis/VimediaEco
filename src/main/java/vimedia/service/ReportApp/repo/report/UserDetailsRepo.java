package vimedia.service.ReportApp.repo.report;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.report.User;

public interface UserDetailsRepo extends JpaRepository<User,String> {
}
