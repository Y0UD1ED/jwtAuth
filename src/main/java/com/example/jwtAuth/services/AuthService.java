package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.TokenDAO;
import com.example.jwtAuth.dtos.JwtRequest;
import com.example.jwtAuth.dtos.JwtResponse;
import com.example.jwtAuth.dtos.RefreshJwtRequest;
import com.example.jwtAuth.dtos.UserDto;
import com.example.jwtAuth.models.*;
import com.example.jwtAuth.utils.JwtTokenUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class AuthService {



   private final TokenService tokenService;

   private final UserService userService;

   private final PasswordEncoder passwordEncoder;
   private final AuthenticationManager authenticationManager;


    public AuthService(TokenService tokenService, AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtResponse login(JwtRequest authRequest){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        ExtendUserDetails user= (ExtendUserDetails) authentication.getPrincipal();
        String accessToken=tokenService.generateAccessToken(user);
        String refreshToken= tokenService.generateRefreshToken(user);
        return new JwtResponse(accessToken,refreshToken);
    }

    public JwtResponse getAccessToken(RefreshJwtRequest authRequest) {
       String accessToken=tokenService.generateAccessToken(authRequest.getRefreshToken());
        return new JwtResponse(accessToken, null);
    }

    public JwtResponse getRefreshToken(RefreshJwtRequest authRequest) {
        ExtendUserDetails user= getUserFromContext();
        String accessToken=tokenService.generateAccessToken(user);
        String refreshToken= tokenService.generateRefreshToken(user);
        return new JwtResponse(accessToken, refreshToken);

    }

    public JwtResponse regUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ExtendUserDetails userDetails = userService.createUser(user);
        String accessToken=tokenService.generateAccessToken(userDetails);
        String refreshToken= tokenService.generateRefreshToken(userDetails);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<String> tryAs() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(5000L);
        return CompletableFuture.completedFuture("ok");
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<String> tryAs2() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(5000L);
        return CompletableFuture.completedFuture("ok");
    }

    public void logout() {
       ExtendUserDetails user=getUserFromContext();
       tokenService.deleteToken(user.getId());
    }

    public ExtendUserDetails getUserFromContext(){
        JwtAuthentication authentication= (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return (ExtendUserDetails) authentication.getPrincipal();
    }

}
