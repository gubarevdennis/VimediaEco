package vimedia.service.ReportApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.User;

public interface UserDetailsRepo extends JpaRepository<User,String> {
}
