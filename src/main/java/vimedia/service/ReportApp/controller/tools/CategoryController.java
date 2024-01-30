package vimedia.service.ReportApp.controller.tools;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.report.Views;
import vimedia.service.ReportApp.model.tools.Category;
import vimedia.service.ReportApp.model.tools.ToolSet;
import vimedia.service.ReportApp.repo.tools.CategoryRepo;
import vimedia.service.ReportApp.repo.tools.ToolSetRepo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryRepo categoryRepo;

    @Autowired
    public CategoryController(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }


    // Получаем все события
    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Category> list() {
        return categoryRepo.findAll().stream().sorted(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList()); // сортировка
    }

    // Получаем одно событие
    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public Category getOne(@PathVariable("id") Category category) {
        return category;
    }


    // Создание события
    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryRepo.save(category);
    }

    // Создание события
    @PutMapping("{id}")
    public Category update(@PathVariable("id") Category categoryFromDB, // из базы данных
                        @RequestBody Category category) { // от пользователя

        BeanUtils.copyProperties(category,categoryFromDB,"id"); // заменяет поля кроме id

        return categoryRepo.save(categoryFromDB);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Category category) {
        categoryRepo.delete(category);
    }
}
