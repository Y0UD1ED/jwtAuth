package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.DirectionDAO;
import com.example.jwtAuth.dao.LevelDAO;
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

    private final LevelDAO levelDAO;

    private final DirectionDAO directionDAO;



    public UserService(UserDAO userDAO, LevelDAO levelDAO, DirectionDAO directionDAO) {
        this.userDAO = userDAO;
        this.levelDAO = levelDAO;
        this.directionDAO = directionDAO;
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


    public User findById(Integer id) {
        User user = userDAO.getUserById(id);
        user.setRoles(userDAO.getUsersRoles(id));
        user.setCurrentLevel(levelDAO.getById(user.getCurrentLevel().getId()));
        user.setDirection(directionDAO.getDirectionById(user.getDirection().getId()));
        return user;

    }
}
