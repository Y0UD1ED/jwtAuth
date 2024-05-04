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
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

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
            AuthResponse authResponse=new AuthResponse();
            authResponse.setTokens(authService.login(authRequest));
            User user=userService.findByUsername(authRequest.getUsername());
            UserInfoDto userInfoDto =userMapper.userToUserInfoDto(user);
            authResponse.setUserId(user.getId());
            authResponse.setRoles(user.getRoles());
            return ResponseEntity.ok(authResponse);

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
        try {
            User user = userMapper.RegRequestToUser(regRequest);
            authService.regUser(user, regRequest.getPassword(), regRequest.getConfirmPassword());
            return ResponseEntity.ok("success");
        }catch (RuntimeException e){
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),e.getMessage()),HttpStatus.BAD_REQUEST);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),"Пользователь с таким логином уже существует"),HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok("success logout");
    }
}
