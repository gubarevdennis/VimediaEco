package vimedia.service.ReportApp.repo.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vimedia.service.ReportApp.model.report.Facility;
import vimedia.service.ReportApp.model.report.Report;
import vimedia.service.ReportApp.model.report.SubFacility;
import vimedia.service.ReportApp.model.report.User;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepo extends JpaRepository<Report,Integer> {

    List<Report> findReportsByReportDayAndUser(LocalDate reportDay, User user);

    List<Report> getReportsByFacility(Facility facility);

    List<Report> getReportsBySubFacility(SubFacility subFacility);

    @Query(value = "SELECT * FROM REPORT WHERE " +
            "REPORT.REPORT_DAY BETWEEN date( date_trunc('month', current_date))  + make_interval(0,?1) " +
            " and date( date_trunc('month', current_date)) + make_interval(0,?1 + 1)", nativeQuery = true)
    List<Report> getReportsByMonth(Integer offsetMonth);

    List<Report> getReportsByUser(User user);


    @Query(value = "SELECT * FROM REPORT WHERE REPORT.USER_ID = ?2 AND " +
            "REPORT.REPORT_DAY BETWEEN date( date_trunc('month', current_date))  + make_interval(0,?1) " +
            " and date( date_trunc('month', current_date)) + make_interval(0,?1 + 1)", nativeQuery = true)
    List<Report> getReportsByMonthAndUser(Integer offsetMonth, Integer userId);
}
