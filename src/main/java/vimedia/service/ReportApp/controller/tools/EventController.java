package vimedia.service.ReportApp.controller.tools;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.report.Facility;
import vimedia.service.ReportApp.model.report.Views;
import vimedia.service.ReportApp.model.tools.Event;
import vimedia.service.ReportApp.repo.report.FacilityRepo;
import vimedia.service.ReportApp.repo.tools.EventRepo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventRepo eventRepo;

    @Autowired
    public EventController(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }


    // Получаем все события
    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Event> list() {
        return eventRepo.findAll().stream().sorted(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    // Получаем одно событие
    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public Event getOne(@PathVariable("id") Event event) {
        return event;
    }


    // Создание события
    @PostMapping
    public Event create(@RequestBody Event event) {
        event.setEventTimeAndDate(LocalDateTime.now());
        return eventRepo.save(event);
    }

    // Создание события
    @PutMapping("{id}")
    public Event update(@PathVariable("id") Event eventFromDB, // из базы данных
                           @RequestBody Event event) { // от пользователя

        BeanUtils.copyProperties(event,eventFromDB,"id"); // заменяет поля кроме id

        return eventRepo.save(eventFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Event event) {
        eventRepo.delete(event);
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
