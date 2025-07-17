package web.service;

import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.model.Role;
import web.model.User;
import web.repositories.RoleRep;
import web.repositories.UserRep;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl {

    private final UserRep userRep;
    private final RoleRep roleRep;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRep userRep, RoleRep roleRep, PasswordEncoder passwordEncoder) {
        this.userRep = userRep;
        this.roleRep = roleRep;
        this.passwordEncoder = passwordEncoder;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (User) authentication.getPrincipal(); // или другой способ получения пользователя
        }
        return null; // Либо выбросьте исключение, если пользователь не найден
    }


    public void createAdmin(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);

        Role role = new Role();
        role.setName("ROLE_ADMIN");

        Role role1 = new Role();
        role1.setName("ROLE_USER");

        roleRep.save(role);
        roleRep.save(role1);

        user.setRoles(List.of(role, role1));

        userRep.save(user);
    }

    public void createUser(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        userRep.save(user);
    }

    public void deleteUser(Integer id) {
      userRep.deleteById(Math.toIntExact(id));
    }

    public User getUserByUsername(String username) {
        return userRep.getUserByUsername(username);
    }

    public User getById(Long id) {
        return userRep.getById(Math.toIntExact(id));
    }

    public List<User> getAllUsers() {
        return userRep.findAll();
    }

    public void updateUser(User user) {
        userRep.save(user);
    }
}