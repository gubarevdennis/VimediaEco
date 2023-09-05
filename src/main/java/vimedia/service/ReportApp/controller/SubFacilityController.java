package vimedia.service.ReportApp.controller;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.exceptions.NotFoundExc;
import vimedia.service.ReportApp.model.Facility;
import vimedia.service.ReportApp.model.SubFacility;
import vimedia.service.ReportApp.model.Views;
import vimedia.service.ReportApp.repo.FacilityRepo;
import vimedia.service.ReportApp.repo.SubFacilityRepo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subFacility")
public class SubFacilityController {
    private final SubFacilityRepo subFacilityRepo;
    private final FacilityRepo facilityRepo;

    @Autowired
    public SubFacilityController(SubFacilityRepo subFacilityRepo, FacilityRepo facilityRepo) {
        this.subFacilityRepo = subFacilityRepo;
        this.facilityRepo = facilityRepo;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<SubFacility> list() {
        return subFacilityRepo.findAll().stream().sorted(new Comparator<SubFacility>() {
            @Override
            public int compare(SubFacility o1, SubFacility o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    @GetMapping("/sumTime/{id}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursBySubFacility(@PathVariable("id") SubFacility subFacility) {
        return subFacilityRepo.getSumOfHoursByFacility(subFacility.getId()) ;
    }

    @GetMapping("/sumTime/subFacility/{id}/month/{offsetMonth}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursBySubFacilityAndMonth(@PathVariable("id") SubFacility subFacility, @PathVariable("offsetMonth") Integer offsetMonth) {
        return subFacilityRepo.getSumOfHoursByFacilityAndMonth(subFacility.getId(), offsetMonth) ;
    }

    @GetMapping("/sumTime/subFacility/{id}/user/{userId}/month/{offsetMonth}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursBySubFacilityMonthAndUser(@PathVariable("id") SubFacility subFacility, @PathVariable("offsetMonth") Integer offsetMonth, @PathVariable("userId") Integer userId) {
        return subFacilityRepo.getSumHoursByFacilityMonthAndUser(subFacility.getId(),  userId, offsetMonth) ;
    }

    @GetMapping("/sumTime/subFacility/{id}/user/{userId}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursBySubFacilityAndUser(@PathVariable("id") SubFacility subFacility, @PathVariable("userId") Integer userId) {
        return subFacilityRepo.getSumHoursByFacilityAndUser(subFacility.getId(), userId) ;
    }

    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public SubFacility getOne(@PathVariable("id") SubFacility subFacility) {
        return subFacility;
    }

    @PostMapping
    public SubFacility create(@RequestBody SubFacility subFacility) {
        subFacility.setCreationDate(LocalDateTime.now());
        if (subFacility.getFacility() != null) {
            subFacility.setFacility(facilityRepo.findByName(subFacility.getFacility().getName()).orElseThrow(() -> new UsernameNotFoundException("Подобъект для добавления не нашел корректный объект!" )));
        }
        return subFacilityRepo.save(subFacility);
    }

    @GetMapping("/sumTime/subFacility/{id}/month/{offsetMonth}/typeOfWork/{typeOfWork}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByFacilityMonthAndWorkType(@PathVariable("id") Facility facility, @PathVariable("offsetMonth") Integer offsetMonth, @PathVariable("typeOfWork") Integer typeOfWork) {
        System.out.println(toWord(typeOfWork));

        return subFacilityRepo.getSumOfHoursByFacilityMonthAndWorkType(facility.getId(), offsetMonth, toWord(typeOfWork));
    }

    @GetMapping("/sumTime/subFacility/{id}/user/{userId}/month/{offsetMonth}/typeOfWork/{typeOfWork}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByFacilityMonthUserAndWorkType(@PathVariable("id") Facility facility, @PathVariable("offsetMonth") Integer offsetMonth, @PathVariable("userId") Integer userId, @PathVariable("typeOfWork") Integer typeOfWork) {
        System.out.println(toWord(typeOfWork));
        return subFacilityRepo.getSumHoursByFacilityMonthUserAndWorkType(facility.getId(),  userId, offsetMonth, toWord(typeOfWork));
    }

    @GetMapping("/sumTime/user/{userId}/month/{offsetMonth}")
    @JsonView(Views.IdName.class)
    public List<Integer> listSumHoursByUserAndMonth(@PathVariable("userId") Integer userId, @PathVariable("offsetMonth") Integer offsetMonth) {
        return subFacilityRepo.getSumHoursByUserAndMonth( userId, offsetMonth) ;
    }

    @PutMapping("{id}")
    public SubFacility update(@PathVariable("id") SubFacility subFacilityFromDB, // из базы данных
                           @RequestBody SubFacility subFacility) { // от пользователя

        BeanUtils.copyProperties(subFacility,subFacilityFromDB,"id"); // заменяет поля кроме id

        return subFacilityRepo.save(subFacilityFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") SubFacility subFacility) {
        subFacilityRepo.delete(subFacility);
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

