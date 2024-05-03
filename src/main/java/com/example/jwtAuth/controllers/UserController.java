package com.example.jwtAuth.controllers;


import com.example.jwtAuth.dtos.UserCreateResponse;
import com.example.jwtAuth.dtos.UserDto;
import com.example.jwtAuth.dtos.UserUpdateDto;
import com.example.jwtAuth.exceptions.AppError;
import com.example.jwtAuth.mappers.UserMapper;
import com.example.jwtAuth.models.User;
import com.example.jwtAuth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userDto){
        try{
            User user=userMapper.UserUpdateDtoToUser(userDto);
            User userUpdated =userService.updateUser(user,userDto.getUpdatedEmail());
            UserDto userDtoUpdated=userMapper.userToUserDto(userUpdated);
            return ResponseEntity.ok(userDtoUpdated);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),"Пользователь не найден"),HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
            User user=userMapper.userDtoToUser(userDto);
            String password=generatePassword(10);
            user.setPassword(passwordEncoder.encode(password));
            userService.createFullUser(user);
            UserCreateResponse userCreateResponse=new UserCreateResponse(user.getUsername(),password);
            return ResponseEntity.ok(userCreateResponse);
    }

    public static String generatePassword(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}