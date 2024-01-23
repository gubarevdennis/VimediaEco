package vimedia.service.ReportApp.controller.tools;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
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

@RestController
@RequestMapping("/api")
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

    // Фильтрация и пагинация
    @GetMapping("/tool")
    @JsonView(Views.IdName.class)
    public List<Tool> findByName(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                 @RequestParam(value = "limit",required = false) Integer limit,
                                 @RequestParam(value = "name", defaultValue = "all", required = false) String name,
                                 @RequestParam(value = "article", defaultValue = "all", required = false) String article,
                                 @RequestParam(value = "facilityname", defaultValue = "all",required = false) String facilityName,
                                 @RequestParam(value = "username",defaultValue = "all", required = false) String userName,
                                 @RequestParam(value = "category",defaultValue = "all", required = false) String category)
    {
        List<Tool> tools;

        if (limit != null) {
            tools = toolRepo.findAll(
                    PageRequest.of(offset, limit, Sort.by("facility"))
            ).stream().collect(Collectors.toList());
        } // сортировка}
        else {
            tools = toolRepo.findAll();
        }

        System.out.println(tools);

        tools = tools.stream()
                .filter(t -> {
                    if (t.getId() != 0 && !name.equals("all")) {
                        System.out.println("id " + name);
                        System.out.println("get id " + t.getId());
                        return ( String.valueOf(t.getName()).toLowerCase().startsWith(name.toLowerCase())
                                || String.valueOf(t.getArticle()).toLowerCase().startsWith(name.toLowerCase())
                                || String.valueOf(t.getModel()).toLowerCase().startsWith(name.toLowerCase())
                        );
                    }
                    else return name.equals("all");
                })
                .filter(t -> {
                    if (t.getArticle() != null && !article.equals("all")) {
                        System.out.println("article " + article);
                        System.out.println("get article " + t.getArticle());
                        return t.getArticle().toLowerCase().startsWith(article.toLowerCase());
                    }
                    else return article.equals("all");
                })
                .filter(t -> {
                    if (t.getFacility() != null && !facilityName.equals("all")) {
                        System.out.println("facilityId " + facilityName);
                        System.out.println("t.getFacility().getId() " + t.getFacility().getId());
                        return ("" + t.getFacility().getName()).toLowerCase().startsWith(facilityName.toLowerCase());
                    }
                    else return facilityName.equals("all");
                })
                .filter(t -> {
                    if (t.getUser() != null && !userName.equals("all")) {
                        System.out.println("userid " + userName);
                        return ("" + t.getUser().getName()).toLowerCase().startsWith(userName.toLowerCase());
                    }
                    else return userName.equals("all");
                })
                .filter(t -> {
                    if (t.getCategory() != null && !category.equals("all")) {
                        System.out.println("category " + category);
                        return t.getCategory().toLowerCase().startsWith(category.toLowerCase());
                    }
                    else return category.equals("all");
                })
                .collect(Collectors.toList());

        return tools;
    }

    // Инструменты конкретного человека
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
    @JsonView(Views.IdName.class)
    public Tool update(@PathVariable("id") Tool toolFromDB, // из базы данных
                       @RequestBody Tool tool) { // от пользователя

        BeanUtils.copyProperties(tool,toolFromDB,"id"); // заменяет поля кроме id

        return toolRepo.save(toolFromDB);
    }

    @DeleteMapping("/tool/{id}")
    public void delete(@PathVariable("id") Tool tool) {
        toolRepo.delete(tool);
    }
}
