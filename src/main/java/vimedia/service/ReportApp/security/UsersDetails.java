//package vimedia.service.ReportApp.security;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import vimedia.service.ReportApp.model.User;
//
//import java.util.Collection;
//import java.util.Collections;
//
//// Класс обертка для security
//public class UsersDetails implements UserDetails {
//
//    private final User user;
//
//    @Autowired
//    public UsersDetails(User user) {
//        this.user = user;
//    }
//
//
//    // Будем возвращать коллекцию прав для пользователя
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // если нужно настроить действия а не роли - нужно вернуть список из действий
//         return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
//    }
//
//    @Override
//    public String getPassword() {
//        return this.user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return this.user.getName();
//    }
//
//    // Не прсрочен
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    // Не заблокирован
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    // Не просрочен
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    // Включен
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    // Нужно чтобы получаить данные аутентифицированного пользователя
//    public User getUsers() {
//        return this.user;
//    }
//}
