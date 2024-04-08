package vimedia.service.ReportApp.controller.tools;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.report.Views;
import vimedia.service.ReportApp.model.tools.Event;
import vimedia.service.ReportApp.model.tools.ToolSet;
import vimedia.service.ReportApp.repo.tools.EventRepo;
import vimedia.service.ReportApp.repo.tools.ToolSetRepo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/toolset")
public class ToolSetController {
    private final ToolSetRepo toolSetRepo;

    @Autowired
    public ToolSetController(ToolSetRepo toolSetRepo) {
        this.toolSetRepo = toolSetRepo;
    }


    // Получаем все события
    @GetMapping
    @JsonView(Views.IdName.class)
    public List<ToolSet> list() {
        return toolSetRepo.findAll().stream().sorted(new Comparator<ToolSet>() {
            @Override
            public int compare(ToolSet o1, ToolSet o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    // Получаем одно событие
    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public ToolSet getOne(@PathVariable("id") ToolSet toolSet) {
        return toolSet;
    }


    // Создание события
    @PostMapping
    public ToolSet create(@RequestBody ToolSet toolSet) {
        return toolSetRepo.save(toolSet);
    }

    // Создание события
    @PutMapping("{id}")
    public ToolSet update(@PathVariable("id") ToolSet toolSetFromDB, // из базы данных
                        @RequestBody ToolSet toolSet) { // от пользователя

        BeanUtils.copyProperties(toolSet,toolSetFromDB,"id"); // заменяет поля кроме id

        return toolSetRepo.save(toolSetFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") ToolSet toolSet) {
        toolSetRepo.delete(toolSet);
    }
}
