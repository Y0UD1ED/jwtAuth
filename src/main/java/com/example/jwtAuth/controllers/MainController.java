package com.example.jwtAuth.controllers;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.services.AuthService;
import com.example.jwtAuth.services.UserService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@RestController
@EnableAsync
public class MainController {

    private UserService userService;

    public MainController(UserService userServicer) {
        this.userService = userService;

    }

    @GetMapping("/unsecured")
    public String unsecuredData(Authentication tok) {
        JwtAuthentication token = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        System.out.println(token.getAuthorities().stream().collect(Collectors.toList()).get(0).getAuthority());
        ExtendUserDetails user = (ExtendUserDetails) token.getPrincipal();
        return user.getUsername();
    }

    @GetMapping("/secured")
    public String securedData() {
        System.out.println("Execute method asynchronously - "
                + Thread.currentThread().getName());
        return "securedData!";
    }



}
