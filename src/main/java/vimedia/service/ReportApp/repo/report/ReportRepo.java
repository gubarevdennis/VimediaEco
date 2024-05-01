package vimedia.service.ReportApp.repo.report;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
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


    @Modifying
    @Transactional
    @Query(value = "UPDATE REPORT SET COST = hours_of_working * ?4 / 8 WHERE USER_ID = ?1" +
            "    AND REPORT.REPORT_DAY >= TO_DATE(?2, 'YYYY-MM-DD')\n" +
            "    AND REPORT.REPORT_DAY <= TO_DATE(?3, 'YYYY-MM-DD')" ,
            nativeQuery = true)
    Integer replaceCoastByActualSalaryWithHoursOfWorking(Long userId, String dateFrom, String dateTo, int salary);

    @Query(value = "SELECT * FROM REPORT WHERE REPORT.USER_ID = ?1 " +
            "  AND REPORT.REPORT_DAY >= TO_DATE(?2, 'YYYY-MM-DD') AND REPORT.REPORT_DAY <= TO_DATE(?3, 'YYYY-MM-DD') ",
            nativeQuery = true)
    List<Report> showCoastByActualSalaryWithHoursOfWorking(Long userId, String dateFrom, String dateTo, int salary);
}
