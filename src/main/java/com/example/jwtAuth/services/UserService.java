package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.DirectionDAO;
import com.example.jwtAuth.dao.LevelDAO;
import com.example.jwtAuth.dao.RoleDAO;

import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.models.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;

    private final RoleDAO roleDAO;

    private final LevelDAO levelDAO;

    private final DirectionDAO directionDAO;

    public UserService(UserDAO userDAO, RoleDAO roleDAO, LevelDAO levelDAO, DirectionDAO directionDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.levelDAO = levelDAO;
        this.directionDAO = directionDAO;
    }

    public User findByUsername(String username){
        User user=userDAO.show(username);
        user.setRoles(roleDAO.show(user.getId()));
        user.setDirection(directionDAO.findById(user.getDirection().getId()));
        user.setCurrentLevel(levelDAO.findById(user.getCurrentLevel().getId()));
        return user;
    }

    public ExtendUserDetails createUser(User user){
        Boolean check=userDAO.check(user.getUsername());
        if(check){
            throw new UsernameNotFoundException(String.format("User '%s' already exists",user.getUsername()));
        }
        userDAO.save(user);
        user.setId(userDAO.getUserId(user.getUsername()));
        roleDAO.setRole(user.getId(),user.getRoles().stream().toList().get(0).getId());
        return new ExtendUserDetails(user.getId(),user.getUsername(),user.getPassword(),user.getRoles());
    }

    public User updateUser(User userUpdated){
        JwtAuthentication authentication= (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        ExtendUserDetails user= (ExtendUserDetails) authentication.getPrincipal();
        if(findByUsername(userUpdated.getUsername())==null){
            throw new UsernameNotFoundException(String.format("User '%s' not founded",user.getUsername()));
        }
        userUpdated.setId(user.getId());
        userDAO.update(userUpdated);
        return findByUsername(user.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException(String.format("User '%s' not founded",username));
        }
        return new ExtendUserDetails(user.getId(),user.getUsername(),user.getPassword(),user.getRoles());
    }
    private Collection<? extends GrantedAuthority> mapRolesToAth(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).toList();
    }
}
