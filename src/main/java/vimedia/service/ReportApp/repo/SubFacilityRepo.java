package vimedia.service.ReportApp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vimedia.service.ReportApp.model.Facility;
import vimedia.service.ReportApp.model.SubFacility;

import java.util.List;
import java.util.Optional;

public interface SubFacilityRepo extends JpaRepository<SubFacility, Integer> {
    Optional<SubFacility> findByName(String name);

    @Query(value = "SELECT SUM(HOURS_OF_WORKING) FROM REPORT LEFT JOIN FACILITY ON FACILITY.ID=REPORT.FACILITY_ID", nativeQuery = true)
    List<Integer> getSumOfHoursByFacilities();

    @Query(value = "SELECT SUM(HOURS_OF_WORKING) FROM REPORT LEFT JOIN FACILITY ON FACILITY.ID=REPORT.USER_ID", nativeQuery = true)
    List<Integer> getSumOfHoursByUsers();

    @Query(value = "SELECT SUM(HOURS_OF_WORKING) FROM REPORT LEFT JOIN FACILITY ON FACILITY.ID=REPORT.FACILITY_ID WHERE FACILITY.ID =?1 ", nativeQuery = true)
    List<Integer> getSumOfHoursByFacility(Integer facilityId);

    @Query(value = "SELECT SUM(HOURS_OF_WORKING) FROM REPORT LEFT JOIN FACILITY ON FACILITY.ID=REPORT.FACILITY_ID WHERE FACILITY.ID =?1 AND REPORT.USER_ID = ?2", nativeQuery = true)
    List<Integer> getSumHoursByFacilityAndUser(Integer facilityId, Integer UserId);

    @Query(value = "SELECT SUM(HOURS_OF_WORKING) FROM REPORT LEFT JOIN FACILITY ON FACILITY.ID=REPORT.FACILITY_ID WHERE FACILITY.ID =?1 AND " +
            "REPORT.REPORT_DAY BETWEEN date( date_trunc('month', current_date)) + make_interval(0,?2-2) and date( date_trunc('month', current_date)) + make_interval(0,?2-1)", nativeQuery = true)
    List<Integer> getSumOfHoursByFacilityAndMonth(Integer facilityId, Integer offsetMonth);

    @Query(value = "SELECT SUM(HOURS_OF_WORKING) FROM REPORT LEFT JOIN FACILITY ON FACILITY.ID=REPORT.FACILITY_ID WHERE FACILITY.ID =?1 AND " +
            "REPORT.REPORT_DAY BETWEEN date( date_trunc('month', current_date)) + make_interval(0,?3-2) and date( date_trunc('month', current_date)) + make_interval(0,?3-1) AND REPORT.USER_ID = ?2", nativeQuery = true)
    List<Integer> getSumHoursByFacilityMonthAndUser(Integer facilityId, Integer UserId, Integer offsetMonth);

    @Query(value = "SELECT SUM(HOURS_OF_WORKING) FROM REPORT LEFT JOIN FACILITY ON FACILITY.ID=REPORT.FACILITY_ID WHERE " +
            "REPORT.REPORT_DAY BETWEEN date( date_trunc('month', current_date)) + make_interval(0,?2-2) and date( date_trunc('month', current_date)) + make_interval(0,?2-1) AND REPORT.USER_ID = ?1", nativeQuery = true)
    List<Integer> getSumHoursByUserAndMonth(Integer UserId, Integer offsetMonth);

    @Query(value = "SELECT SUM(HOURS_OF_WORKING) FROM REPORT LEFT JOIN FACILITY ON FACILITY.ID=REPORT.FACILITY_ID WHERE FACILITY.ID =?1 AND " +
            "REPORT.REPORT_DAY BETWEEN date( date_trunc('month', current_date)) + make_interval(0,?2-2) and date( date_trunc('month', current_date)) + make_interval(0,?2-1) AND REPORT.type_of_work=?3", nativeQuery = true)
    List<Integer> getSumOfHoursByFacilityMonthAndWorkType(Integer facilityId, Integer offsetMonth, String typeOfWork);

    @Query(value = "SELECT SUM(HOURS_OF_WORKING) FROM REPORT LEFT JOIN FACILITY ON FACILITY.ID=REPORT.FACILITY_ID WHERE FACILITY.ID =?1 AND " +
            "REPORT.REPORT_DAY BETWEEN date( date_trunc('month', current_date)) + make_interval(0,?3-2) and date( date_trunc('month', current_date)) + make_interval(0,?3-1) AND REPORT.USER_ID = ?2 AND REPORT.type_of_work=?4", nativeQuery = true)
    List<Integer> getSumHoursByFacilityMonthUserAndWorkType(Integer facilityId, Integer UserId, Integer offsetMonth, String typeOfWork);

}
