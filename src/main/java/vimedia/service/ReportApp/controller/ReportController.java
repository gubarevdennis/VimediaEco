package vimedia.service.ReportApp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.Report;
import vimedia.service.ReportApp.model.SubFacility;
import vimedia.service.ReportApp.model.User;
import vimedia.service.ReportApp.model.Views;
import vimedia.service.ReportApp.repo.ReportRepo;
import vimedia.service.ReportApp.repo.SubFacilityRepo;
import vimedia.service.ReportApp.repo.UserRepo;
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

        if (this.hours >= 8) return null;

        return reportRepo.save(report);
    }

    @PutMapping("{id}")
    public Report update(@PathVariable("id") Report reportFromDB, // из базы данных
                           @RequestBody Report report,
                         @AuthenticationPrincipal MyUserDetails myUserDetails) { // от пользователя

        BeanUtils.copyProperties(report,reportFromDB,"id"); // заменяет поля кроме id

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        // Вставляем его id в пользователя для отчета
        user.setId(user.getId());

        report.setUser(user);

        // Проверка что в один день было отправлено отчетов больше 8 часов в сумме
        List<Report> reportsForHoursChek = reportRepo.findReportsByReportDayAndUser(report.getReportDay(), user);

        // Обнуляем часы
        this.hours = 0;

        reportsForHoursChek.forEach(r -> countingHours(r.getHoursOfWorking()));

        if (this.hours >= 8) return null;

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
