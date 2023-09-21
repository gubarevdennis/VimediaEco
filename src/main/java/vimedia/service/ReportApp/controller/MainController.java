package vimedia.service.ReportApp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vimedia.service.ReportApp.model.User;
import vimedia.service.ReportApp.repo.UserRepo;
import vimedia.service.ReportApp.service.MyUserDetails;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainController {

    private final UserRepo userRepo;

    @Autowired
    public MainController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @GetMapping
    public String getIndex(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        HashMap<Object, Object> data = new HashMap<>();

        data.put("name",myUserDetails.getUsername());
        data.put("id", userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!" )).getId());
        data.put("role",myUserDetails.getAuthorities());

        model.addAttribute("frontendData", data);
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        List<User> users = userRepo.findAll().stream().sorted(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("sessionId", session.getId());
        return "login";
    }

}
