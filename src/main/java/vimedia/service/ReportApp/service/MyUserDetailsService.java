package vimedia.service.ReportApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import vimedia.service.ReportApp.model.User;
import vimedia.service.ReportApp.repo.UserRepo;

import java.util.Optional;

@Component
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepo userRepo;

    @Autowired
    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepo.findByName(username);

       return foundUser.map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!" + username));

    }
}
