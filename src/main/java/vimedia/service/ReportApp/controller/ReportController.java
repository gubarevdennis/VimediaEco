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

        // Временно пользователь будет с id 1

        List<User> users = new ArrayList<>();

        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        user.setId(user.getId());

        users.add(user);
        report.setUser(user);

        return reportRepo.save(report);
    }

    @PutMapping("{id}")
    public Report update(@PathVariable("id") Report reportFromDB, // из базы данных
                           @RequestBody Report report) { // от пользователя

        BeanUtils.copyProperties(report,reportFromDB,"id"); // заменяет поля кроме id

        return reportRepo.save(reportFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Report report) {
        reportRepo.delete(report);
    }

}
