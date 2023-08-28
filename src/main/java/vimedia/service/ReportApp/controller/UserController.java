package vimedia.service.ReportApp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vimedia.service.ReportApp.model.Facility;
import vimedia.service.ReportApp.model.User;
import vimedia.service.ReportApp.model.Views;
import vimedia.service.ReportApp.repo.UserRepo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<User> list() {
        return userRepo.findAll().stream().sorted(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @JsonView(Views.IdName.class)
    public User getOne(@PathVariable("id") User user) {
        return user;
    }

    @PostMapping
    @JsonView(Views.IdName.class)
    public User create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @PutMapping("{id}")
    @JsonView(Views.IdName.class)
    public User update(@PathVariable("id") User userFromDB, // из базы данных
                         @RequestBody User user) { // от пользователя

        BeanUtils.copyProperties(user,userFromDB,"id"); // заменяет поля кроме id

        userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(userFromDB);
    }

    @DeleteMapping("{id}")
    @JsonView(Views.IdName.class)
    public void delete(@PathVariable("id") User user) {
        userRepo.delete(user);
    }

}
