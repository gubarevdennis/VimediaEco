package vimedia.service.ReportApp.controller.report;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.report.Facility;
import vimedia.service.ReportApp.model.report.Job;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.report.Views;
import vimedia.service.ReportApp.repo.report.FacilityRepo;
import vimedia.service.ReportApp.repo.report.UserRepo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/facility")
public class FacilityController {
    private final FacilityRepo facilityRepo;
    private final UserRepo userRepo;

    @Autowired
    public FacilityController(FacilityRepo facilityRepo, UserRepo userRepo) {
        this.facilityRepo = facilityRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Facility> list() {
        return facilityRepo.findAll().stream().filter(j -> j.getName() != null).sorted(new Comparator<Facility>() {
            @Override
            public int compare(Facility o1, Facility o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    @GetMapping("/sumTime/{id}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByFacility(@PathVariable("id") Facility facility) {
        return facilityRepo.getSumOfHoursByFacility(facility.getId()) ;
    }

    @GetMapping("/sumTime/facility/{id}/month/{offsetMonth}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByFacilityAndMonth(@PathVariable("id") Facility facility, @PathVariable("offsetMonth") Integer offsetMonth) {
        return facilityRepo.getSumOfHoursByFacilityAndMonth(facility.getId(), offsetMonth) ;
    }

    @GetMapping("/sumTime/facility/{id}/user/{userId}/month/{offsetMonth}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByFacilityMonthAndUser(@PathVariable("id") Facility facility, @PathVariable("offsetMonth") Integer offsetMonth, @PathVariable("userId") Integer userId) {
        return facilityRepo.getSumHoursByFacilityMonthAndUser(facility.getId(),  userId, offsetMonth) ;
    }

    @GetMapping("/sumTime/facility/{id}/user/{userId}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByFacilityAndUser(@PathVariable("id") Facility facility, @PathVariable("userId") Integer userId) {
        return facilityRepo.getSumHoursByFacilityAndUser(facility.getId(), userId) ;
    }

    @GetMapping("/sumTime/user/{userId}/month/{offsetMonth}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByUserAndMonth(@PathVariable("userId") Integer userId, @PathVariable("offsetMonth") Integer offsetMonth) {
        return facilityRepo.getSumHoursByUserAndMonth( userId, offsetMonth) ;
    }

    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public Facility getOne(@PathVariable("id") Facility facility) {
        return facility;
    }

    @PostMapping
    public Facility create(@RequestBody Facility facility) {
        facility.setCreationDate(LocalDateTime.now());
        return facilityRepo.save(facility);
    }

    @GetMapping("/sumTime/facility/{id}/month/{offsetMonth}/typeOfWork/{typeOfWork}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByFacilityMonthAndWorkType(@PathVariable("id") Facility facility, @PathVariable("offsetMonth") Integer offsetMonth, @PathVariable("typeOfWork") Integer typeOfWork) {
        System.out.println(toWord(typeOfWork));

        return facilityRepo.getSumOfHoursByFacilityMonthAndWorkType(facility.getId(), offsetMonth, toWord(typeOfWork));
    }

    @GetMapping("/sumTime/facility/{id}/user/{userId}/month/{offsetMonth}/typeOfWork/{typeOfWork}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByFacilityMonthUserAndWorkType(@PathVariable("id") Facility facility, @PathVariable("offsetMonth") Integer offsetMonth, @PathVariable("userId") Integer userId, @PathVariable("typeOfWork") Integer typeOfWork) {
        System.out.println(toWord(typeOfWork));
        return facilityRepo.getSumHoursByFacilityMonthUserAndWorkType(facility.getId(),  userId, offsetMonth, toWord(typeOfWork));
    }

    // Получаем по пользователю
    @GetMapping("/user/{id}")
    @JsonView(Views.IdName.class)
    public List<Facility> getByUser(@PathVariable("id") User user) {
        return user.getFacilities();
    }

    // Получаем прикрепленные к пользователю обьекты
    @GetMapping("/job/user/{id}")
    @JsonView(Views.IdName.class)
    public List<Facility> getByJobAndUser(@PathVariable("id") User user) {
        return user.getJobs().stream().map(Job::getFacility).filter(Objects::nonNull).distinct().collect
                (Collectors.toList());
    }

    // Получаем по работе
    @GetMapping("/job/{id}")
    @JsonView(Views.IdName.class)
    public Facility getByJob(@PathVariable("id") Job job) {
        return job.getFacility();
    }

    @PutMapping("{id}")
    public Facility update(@PathVariable("id") Facility facilityFromDB, // из базы данных
                           @RequestBody Facility facility) { // от пользователя

//        if(facility.getUser() != null) {
//            facility.setUser(userRepo.findById(facility.getUser().getId().intValue()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!")));
//        }

        BeanUtils.copyProperties(facility,facilityFromDB,"id"); // заменяет поля кроме id



        //TODO Сохраним user если он прилетел вместе с объектом


        return facilityRepo.save(facilityFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Facility facility) {
        facilityRepo.delete(facility);
    }

    public String toWord(Integer typeOfWork) {
        String typeOfWorkByWord;
        switch (typeOfWork) {
            case 1 : typeOfWorkByWord = "Черновой монтаж";
                break;
            case 2 : typeOfWorkByWord = "Рабочее проектирование";
                break;
            case 3 : typeOfWorkByWord = "Авторский надзор";
                break;
            case 4 : typeOfWorkByWord = "Сборка щитов";
                break;
            case 5 : typeOfWorkByWord = "ПНР";
                break;
            case 6 : typeOfWorkByWord = "Сервис";
                break;
            case 7 : typeOfWorkByWord = "Отпуск оплачиваемый";
                break;
            case 8 : typeOfWorkByWord = "Больничный";
                break;
            case 9 : typeOfWorkByWord = "Отпуск без сохранения ЗП";
                break;
            case 10 : typeOfWorkByWord = "Менеджмент";
                break;
            case 11 : typeOfWorkByWord = "Концептуальное проектирование";
                break;
            case 12 : typeOfWorkByWord = "Шефмонтаж";
                break;
            case 13 : typeOfWorkByWord = "Чистовой монтаж";
                break;
            case 14 : typeOfWorkByWord = "Отпуск по семейным обстоятельствам";
                break;
            case 15 : typeOfWorkByWord = "Расключение шкафов";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + typeOfWork);
        }
        return typeOfWorkByWord;
    }

}

