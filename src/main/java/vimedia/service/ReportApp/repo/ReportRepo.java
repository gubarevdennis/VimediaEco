package vimedia.service.ReportApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vimedia.service.ReportApp.model.Facility;
import vimedia.service.ReportApp.model.Report;
import vimedia.service.ReportApp.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportRepo extends JpaRepository<Report,Integer> {

    List<Report> findReportsByReportDayAndUser(LocalDate reportDay, User user);

    @Query(value = "SELECT * FROM REPORT WHERE " +
            "REPORT.REPORT_DAY BETWEEN date( date_trunc('month', current_date))  + make_interval(0,?1) " +
            " and date( date_trunc('month', current_date)) + make_interval(0,?1 + 1)", nativeQuery = true)
    List<Report> getReportsByMonth(Integer offsetMonth);
}
