package vimedia.service.ReportApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name );
}
