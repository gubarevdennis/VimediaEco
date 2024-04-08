package vimedia.service.ReportApp.repo.job;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vimedia.service.ReportApp.model.report.Facility;
import vimedia.service.ReportApp.model.report.Job;

import java.util.List;
import java.util.Optional;

public interface JobRepo extends JpaRepository<Job, Integer> {

    Optional<Job> findByName(String name);

}
