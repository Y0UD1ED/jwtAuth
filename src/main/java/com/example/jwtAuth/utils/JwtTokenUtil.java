package com.example.jwtAuth.utils;


import com.example.jwtAuth.models.BaseUser;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.Role;
import com.example.jwtAuth.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;


    public String generateToken(ExtendUserDetails user){
        Map<String,Object> claims=new HashMap<>();
        List<Map<String,Object>> roleList=user.getRoles().stream()
                .map(role->new HashMap<String,Object>(){{
                    put("id",role.getId());
                    put("name",role.getAuthority());}})
                .collect(Collectors.toList());
        //roleList.add("ROLE_USER2");
        claims.put("roles",roleList);
        claims.put("id",user.getId());
        Date issuedDate=new Date();
        Date expiredDate=new Date(issuedDate.getTime()+lifetime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String generateRefreshToken(ExtendUserDetails user) {
        Date issuedDate=new Date();
        Date expiredDate=new Date(issuedDate.getTime()+lifetime.toMillis());
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",user.getId());
        List<Map<String,Object>> roleList=user.getRoles().stream()
                .map(role->new HashMap<String,Object>(){{
                    put("id",role.getId());
                    put("name",role.getAuthority());}})
                .collect(Collectors.toList());
        claims.put("roles",roleList);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).build().parseSignedClaims(token).getPayload();
    }

    public String getUsername(String token){
        return  getAllClaimsFromToken(token).getSubject();
    }
    public Integer getId(String token){
        return  getAllClaimsFromToken(token).get("id",Integer.class);
    }
    public  List<Role> getRoles(String token){
        List<Map<String,Object>> roles=getAllClaimsFromToken(token).get("roles",List.class);
        return roles.stream().map(role->new Role((Integer) role.get("id"),(String) role.get("name"))).collect(Collectors.toList());
    }

       // return getAllClaimsFromToken(token).get("roles",List.class);
}
