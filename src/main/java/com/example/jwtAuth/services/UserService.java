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
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
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
        User user=userDAO.getUserByLogin(username);
        user.setRoles(roleDAO.getUsersRoles(user.getId()));
        user.setDirection(directionDAO.getDirectionById(user.getDirection().getId()));
        user.setCurrentLevel(levelDAO.getById(user.getCurrentLevel().getId()));
        return user;
    }

    public ExtendUserDetails createUser(User user) throws AuthenticationException {
        boolean check=userDAO.isUserExist(user.getUsername());
        if(check){
            throw new AuthenticationException(String.format("User '%s' already exists",user.getUsername()));
        }
        userDAO.addUser(user);
        user.setId(userDAO.getUserId(user.getUsername()));
        roleDAO.setUserRole(user.getId());
        user.setScores(0);
        user.setRoles(roleDAO.getUsersRoles(user.getId()));
        return new ExtendUserDetails(user.getId(),user.getUsername(),user.getPassword(),user.getRoles());
    }

    public void createFullUser(User user){
        boolean check=userDAO.isUserExist(user.getUsername());
        if(check){
            throw new UsernameNotFoundException(String.format("User '%s' already exists",user.getUsername()));
        }
        userDAO.addFullUser(user);
        Integer id=userDAO.getUserId(user.getUsername());
        roleDAO.setUserRole(id);
    }

    public User updateUser(User userUpdated,String updatedUsername){
        JwtAuthentication authentication= (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        ExtendUserDetails user= (ExtendUserDetails) authentication.getPrincipal();
        boolean check=userDAO.isUserExist(userUpdated.getUsername());
        if(!check){
            throw new UsernameNotFoundException(String.format("User '%s' not founded",user.getUsername()));
        }
        userUpdated.setUsername(updatedUsername);
        userUpdated.setId(user.getId());
        userDAO.updateUser(userUpdated);
        return userUpdated;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean check=userDAO.isUserExist(username);
        if(!check){
            throw new UsernameNotFoundException(String.format("User '%s' not founded",username));
        }
        User user=findByUsername(username);
        return new ExtendUserDetails(user.getId(),user.getUsername(),user.getPassword(),user.getRoles());
    }
    private Collection<? extends GrantedAuthority> mapRolesToAth(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).toList();
    }


}
