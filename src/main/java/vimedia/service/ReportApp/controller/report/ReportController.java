package vimedia.service.ReportApp.controller.report;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.report.Report;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.report.Views;
import vimedia.service.ReportApp.repo.report.ReportRepo;
import vimedia.service.ReportApp.repo.report.SubFacilityRepo;
import vimedia.service.ReportApp.repo.report.UserRepo;
import vimedia.service.ReportApp.service.MyUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportRepo reportRepo;
    private final SubFacilityRepo subFacilityRepo;
    private final UserRepo userRepo;
    private int hours;

    @Autowired
    public ReportController(ReportRepo reportRepo, UserRepo userRepo, SubFacilityRepo subFacilityRepo) {
        this.reportRepo = reportRepo;
        this.userRepo = userRepo;
        this.subFacilityRepo = subFacilityRepo;
    }

    @GetMapping()
    @JsonView(Views.IdName.class)
    public List<Report> list() {
        return reportRepo.findAll();
    }

    @GetMapping("{id}")
    public Report getOne(@PathVariable("id") Report report) {
        return report;
    }

    @GetMapping("/month/{offsetMonth}")
    @JsonView(Views.IdName.class)
    public List<Report> listByMonth(@PathVariable("offsetMonth") Integer offsetMonth) {
        return reportRepo.getReportsByMonth(offsetMonth);
    }

    @GetMapping("/user")
    @JsonView(Views.IdName.class)
    public List<Report> listByUser(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));
        return reportRepo.getReportsByUser(user);
    }

    @GetMapping("/month/user/{offsetMonth}")
    @JsonView(Views.IdName.class)
    public List<Report> listByMonthAndUser(@PathVariable("offsetMonth") Integer offsetMonth, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        int userId;
        if (user != null) {
            userId = Math.toIntExact(user.getId());
            return reportRepo.getReportsByMonthAndUser(offsetMonth, userId);
        } else return null;
    }

    @PostMapping
    @JsonView(Views.IdName.class)
    public Report create(@RequestBody Report report, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        report.setCreationDate(LocalDateTime.now());
        // Добавляем подобъект
        if (report.getSubFacility() != null) {
            report.setSubFacility(subFacilityRepo.findByName(report.getSubFacility().getName()).orElse(null));
        }

        // Проверяем указан ли день отчета
        if (report.getReportDay() == null) {
            report.setReportDay(LocalDate.now());
        }

        List<User> users = new ArrayList<>();

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        // Вставляем его id в пользователя для отчета
        user.setId(user.getId());

        users.add(user);
        report.setUser(user);

        // Проверка что в один день было отправлено отчетов больше 8 часов в сумме
        List<Report> reportsForHoursChek = reportRepo.findReportsByReportDayAndUser(report.getReportDay(), user);

        System.out.println(reportsForHoursChek);

        // Обнуляем часы
        this.hours = 0;

        reportsForHoursChek.forEach(r -> countingHours(r.getHoursOfWorking()));

        System.out.println(this.hours);

        if (this.hours + report.getHoursOfWorking() > 8) return null;

        return reportRepo.save(report);
    }

    @PostMapping("{id}")
    @JsonView(Views.IdName.class)
    public Report update(@PathVariable("id") Report reportFromDB, // из базы данных
                         @RequestBody Report report,
                         @AuthenticationPrincipal MyUserDetails myUserDetails) { // от пользователя

        // Добавляем подобъект
        if (report.getSubFacility() != null) {
            report.setSubFacility(subFacilityRepo.findByName(report.getSubFacility().getName()).orElse(null));
        }

        List<User> users = new ArrayList<>();

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        // Вставляем его id в пользователя для отчета
        user.setId(user.getId());

        users.add(user);
        report.setUser(user);

        Report reportFromDBBeforeCopy = reportFromDB;

        BeanUtils.copyProperties(report,reportFromDB,"id", "reportDay"); // заменяет поля кроме id

        // Проверка что в один день было отправлено отчетов больше 8 часов в сумме
        List<Report> reportsForHoursChek = reportRepo.findReportsByReportDayAndUser(report.getReportDay(), user);

        // Обнуляем часы
        this.hours = 0;

        reportsForHoursChek.forEach(r -> countingHours(r.getHoursOfWorking()));

        System.out.println("report.getHoursOfWorking() " + report.getHoursOfWorking());
        System.out.println("reportFromDB.getHoursOfWorking() " + reportFromDB.getHoursOfWorking());
        System.out.println("this.hours " + this.hours);
        System.out.println("Result hours " + (this.hours - report.getHoursOfWorking() + reportFromDB.getHoursOfWorking()));

        if (this.hours + report.getHoursOfWorking() - reportFromDBBeforeCopy.getHoursOfWorking() > 8) return null;

        return reportRepo.save(reportFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Report report) {
        reportRepo.delete(report);
    }


    public void countingHours(int hours) {
        this.hours = this.hours + hours;
    }
}
