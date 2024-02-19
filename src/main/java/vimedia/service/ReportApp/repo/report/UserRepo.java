package vimedia.service.ReportApp.repo.report;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.report.Job;
import vimedia.service.ReportApp.model.report.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
    Optional<User> findByTelegramId(String telegramId);
}
