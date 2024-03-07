package vimedia.service.ReportApp.repo.bonus;


import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.bonus.Bonus;
import vimedia.service.ReportApp.model.report.Job;

import java.util.Optional;

public interface BonusRepo extends JpaRepository<Bonus, Integer> {

    Optional<Bonus> findByName(String name);

}
