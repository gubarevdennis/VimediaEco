package vimedia.service.ReportApp.controller.tools;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.report.Views;
import vimedia.service.ReportApp.model.tools.Event;
import vimedia.service.ReportApp.model.tools.Tool;
import vimedia.service.ReportApp.repo.tools.EventRepo;
import vimedia.service.ReportApp.repo.tools.ToolRepo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tool")
public class ToolController {
    private final ToolRepo toolRepo;

    @Autowired
    public ToolController(ToolRepo toolRepo) {
        this.toolRepo = toolRepo;
    }


    // Получаем все события
    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Tool> list() {
        return toolRepo.findAll().stream().sorted(new Comparator<Tool>() {
            @Override
            public int compare(Tool o1, Tool o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    // Получаем одно событие
    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public Tool getOne(@PathVariable("id") Tool tool) {
        return tool;
    }


    // Создание события
    @PostMapping
    public Tool create(@RequestBody Tool tool) {
        return toolRepo.save(tool);
    }

    // Создание события
    @PutMapping("{id}")
    public Tool update(@PathVariable("id") Tool toolFromDB, // из базы данных
                        @RequestBody Tool tool) { // от пользователя

        BeanUtils.copyProperties(tool,toolFromDB,"id"); // заменяет поля кроме id

        return toolRepo.save(toolFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Tool tool) {
        toolRepo.delete(tool);
    }
}
