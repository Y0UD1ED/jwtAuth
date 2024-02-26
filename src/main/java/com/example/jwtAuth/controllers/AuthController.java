package com.example.jwtAuth.controllers;

import com.example.jwtAuth.dtos.*;
import com.example.jwtAuth.exceptions.AppError;
import com.example.jwtAuth.mappers.UserMapper;
import com.example.jwtAuth.models.User;
import com.example.jwtAuth.services.AuthService;
import com.example.jwtAuth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthController {


    private final AuthService authService;

    private final UserService userService;

    private final UserMapper userMapper;

    public AuthController(AuthService authService,UserService userService,UserMapper userMapper){

        this.authService = authService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> sendToken(@RequestBody JwtRequest authRequest){
        try{
            List<Object> list = new ArrayList<>();
            JwtResponse jwtResponse=authService.login(authRequest);
            list.add(jwtResponse);
            User user=userService.findByUsername(authRequest.getUsername());
            UserDto userDto=userMapper.userToUserDto(user);
            list.add(userDto);
            return ResponseEntity.ok(list);

        }catch (BadCredentialsException e){
            return  new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),"Неверный логин и/или пароль"),HttpStatus.BAD_REQUEST);

        }


    }

    @PostMapping("/getAccessToken")
    public ResponseEntity<?> getAccessToken(@RequestBody RefreshJwtRequest authRequest) {
        JwtResponse jwtResponse=authService.getAccessToken(authRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/getRefreshToken")
    public ResponseEntity<?> getRefreshToken(@RequestBody RefreshJwtRequest authRequest) {
        JwtResponse jwtResponse=authService.getRefreshToken(authRequest);
        return ResponseEntity.ok(jwtResponse);

    }

    @PostMapping("/reg")
    public ResponseEntity<?> regUser(@RequestBody RegRequest regRequest){
        User user=userMapper.RegRequestToUser(regRequest);
        JwtResponse jwtResponse=authService.regUser(user);
        List<Object> list=new ArrayList<>();
        list.add(jwtResponse);
        list.add(user);
        return ResponseEntity.ok(list);

    }

    @RequestMapping(value = "/update_user", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto){
        try{
            User user=userMapper.userDtoToUser(userDto);
            User userUpdated =userService.updateUser(user);
            UserDto userDtoUpdated=userMapper.userToUserDto(userUpdated);
            return ResponseEntity.ok(userDtoUpdated);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),"Пользователь не найден"),HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok("success logout");
    }
}
