package vimedia.service.ReportApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vimedia.service.ReportApp.model.Report;

public interface ReportRepo extends JpaRepository<Report,Integer> {
}
