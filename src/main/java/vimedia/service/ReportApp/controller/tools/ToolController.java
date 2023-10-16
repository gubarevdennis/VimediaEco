package vimedia.service.ReportApp.controller.tools;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.report.Facility;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.report.Views;
import vimedia.service.ReportApp.model.tools.Event;
import vimedia.service.ReportApp.model.tools.Tool;
import vimedia.service.ReportApp.repo.report.FacilityRepo;
import vimedia.service.ReportApp.repo.report.UserRepo;
import vimedia.service.ReportApp.repo.tools.EventRepo;
import vimedia.service.ReportApp.repo.tools.ToolRepo;
import vimedia.service.ReportApp.service.MyUserDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class ToolController {
    private final ToolRepo toolRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final FacilityRepo facilityRepo;

    @Autowired
    public ToolController(ToolRepo toolRepo, EventRepo eventRepo, UserRepo userRepo, FacilityRepo facilityRepo) {
        this.toolRepo = toolRepo;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.facilityRepo = facilityRepo;
    }


    // Получаем все интсрументы
    @GetMapping("/tool")
    @JsonView(Views.IdName.class)
    public List<Tool> list() {
        return toolRepo.findAll().stream().sorted(new Comparator<Tool>() {
            @Override
            public int compare(Tool o1, Tool o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }


    @GetMapping("/tool/ofcurrentuser")
    @JsonView(Views.IdName.class)
    public List<Tool> listOfCurrentUser(@AuthenticationPrincipal MyUserDetails myUserDetails) {

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        return toolRepo.findByUser(user).stream().sorted(new Comparator<Tool>() {
            @Override
            public int compare(Tool o1, Tool o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    // Получаем инструменты для подтверждения по конкретному человеку
    @GetMapping("/toolforsend/confirm")
    @JsonView(Views.IdName.class)
    public List<Tool> getToolToConfirm(@AuthenticationPrincipal MyUserDetails myUserDetails) {

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        // Вставляем его id в пользователя для отчета
        user.setId(user.getId());

        List <Event> eventsByUser = eventRepo.findByToUser(user.getName());

        System.out.println("eventsByUser");
        System.out.println(eventsByUser);

        List <Event> eventsByUserAndConfirm = eventsByUser.stream()
                .filter(e -> e.getName().equals("Направлен на передачу"))
                .collect(Collectors.toList());

        System.out.println("eventsByUserAndConfirm");
        System.out.println(eventsByUserAndConfirm);

        List <Tool> toolsByUser = new ArrayList<>();

        for (Event event : eventsByUserAndConfirm) {
            if (event.getTool() != null) {
                toolsByUser.add(toolRepo.findById(event.getTool().getId()).orElse(null));
                System.out.println("event.getTool()");
                System.out.println(event.getTool());
            }
        }

        System.out.println("toolsByUser");
        System.out.println(toolsByUser);

        return toolsByUser;
    }

    // Получаем одно событие
    @GetMapping("/tool/{id}")
    @JsonView(Views.IdName.class)
    public Tool getOne(@PathVariable("id") Tool tool) {
        return tool;
    }

    // Создание события
    @PostMapping("/tool")
    public Tool create(@RequestBody Tool tool) {
        return toolRepo.save(tool);
    }

    // Создание события
    @PutMapping("/tool/{id}")
    public Tool update(@PathVariable("id") Tool toolFromDB, // из базы данных
                       @RequestBody Tool tool) { // от пользователя

//        if (tool.getFacility() != null)
//        if (tool.getFacility().getId() == 0) {
//            // Если не указан id для facility то он найдется по имени
//            facilityRepo.findByName(tool.getFacility().getName()).ifPresent(tool::setFacility);
//            System.out.println(tool);
//        }

        BeanUtils.copyProperties(tool,toolFromDB,"id"); // заменяет поля кроме id

        return toolRepo.save(toolFromDB);
    }

    @DeleteMapping("/tool/{id}")
    public void delete(@PathVariable("id") Tool tool) {
        toolRepo.delete(tool);
    }
}
