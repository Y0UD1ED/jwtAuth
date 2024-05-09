package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.dtos.CourseMapResponse;
import com.example.jwtAuth.dtos.CourseResponse;
import com.example.jwtAuth.dtos.ShortCourseDto;
import com.example.jwtAuth.models.*;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;

    private final BonusesService bonusesService;
    private final CourseService courseService;

    private final QuestModuleService questModuleService;


    public UserService(UserDAO userDAO, CourseService courseService, QuestModuleService questModuleService, BonusesService bonusesService) {
        this.userDAO = userDAO;
        this.courseService = courseService;
        this.questModuleService = questModuleService;
        this.bonusesService = bonusesService;
    }

    public User findByUsername(String username) {
        User user = userDAO.getUserByLogin(username);
        user.setRoles(userDAO.getUsersRoles(user.getId()));
        return user;
    }

    public void createUser(User user) throws AuthenticationException {
        boolean check = userDAO.isUserExist(user.getLogin());
        if (check) {
            throw new AuthenticationException(String.format("User '%s' already exists", user.getLogin()));
        }
        user.setBalance(0);
        Integer userId = userDAO.addUser(user);
        userDAO.setUserRole(userId);

    }

    public void createFullUser(User user) {
        boolean check = userDAO.isUserExist(user.getLogin());
        if (check) {
            throw new UsernameNotFoundException(String.format("User '%s' already exists", user.getLogin()));
        }
        Integer userId = userDAO.addFullUser(user);
        userDAO.setUserRole(userId);
    }

    public User updateUser(User userUpdated) {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        ExtendUserDetails user = (ExtendUserDetails) authentication.getPrincipal();
        boolean check = userDAO.isUserExist(user.getId());
        if (!check) {
            throw new UsernameNotFoundException(String.format("User '%s' not founded", user.getUsername()));
        }
        userDAO.updateUser(userUpdated);
        return userUpdated;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean check = userDAO.isUserExist(username);
        if (!check) {
            throw new UsernameNotFoundException(String.format("User '%s' not founded", username));
        }
        User user = findByUsername(username);
        return new ExtendUserDetails(user.getId(), user.getLogin(), user.getPassword(), user.getRoles());
    }

    private Collection<? extends GrantedAuthority> mapRolesToAth(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).toList();
    }









}
