package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dtos.JwtRequest;
import com.example.jwtAuth.dtos.JwtResponse;
import com.example.jwtAuth.dtos.RefreshJwtRequest;
import com.example.jwtAuth.models.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        ExtendUserDetails user = (ExtendUserDetails) authentication.getPrincipal();
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);
        return new JwtResponse(accessToken, refreshToken);
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

    public void regUser(User user, String password, String confirmPassword) throws AuthenticationException {
        if(!password.equals(confirmPassword)){
            System.out.println(confirmPassword+" "+password);
            throw new RuntimeException("Passwords do not match");

        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.createUser(user);

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
