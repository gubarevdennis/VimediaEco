package vimedia.service.ReportApp.controller;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.Facility;
import vimedia.service.ReportApp.model.Views;
import vimedia.service.ReportApp.repo.FacilityRepo;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/facility")
public class FacilityController {
    private final FacilityRepo facilityRepo;

    @Autowired
    public FacilityController(FacilityRepo facilityRepo) {
        this.facilityRepo = facilityRepo;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Facility> list() {
        return facilityRepo.findAll() ;
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

    @PutMapping("{id}")
    public Facility update(@PathVariable("id") Facility facilityFromDB, // из базы данных
                           @RequestBody Facility facility) { // от пользователя

        BeanUtils.copyProperties(facility,facilityFromDB,"id"); // заменяет поля кроме id

        return facilityRepo.save(facilityFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Facility facility) {
        facilityRepo.delete(facility);
    }

    public String toWord(Integer typeOfWork) {
        String typeOfWorkByWord;
        switch (typeOfWork) {
            case 1 : typeOfWorkByWord = "Монтаж";
                break;
            case 2 : typeOfWorkByWord = "Проект";
                break;
            case 3 : typeOfWorkByWord = "Менеджмент";
                break;
            case 4 : typeOfWorkByWord = "Сборка";
                break;
            case 5 : typeOfWorkByWord = "ПНР";
                break;
            case 6 : typeOfWorkByWord = "Сервис";
                break;
            case 7 : typeOfWorkByWord = "Отпуск";
                break;
            case 8 : typeOfWorkByWord = "Больничный";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + typeOfWork);
        }
        return typeOfWorkByWord;
    }

}
