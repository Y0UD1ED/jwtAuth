package com.example.jwtAuth.configs;


import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.Role;
import com.example.jwtAuth.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component

public class JwtRequestFilter extends OncePerRequestFilter {


    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        Integer id=null;
        String jwt = null;
        List<Role> roles=null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsername(jwt);
                roles=jwtTokenUtil.getRoles(jwt);
                id=jwtTokenUtil.getId(jwt);
            } catch (ExpiredJwtException e) {
                System.out.println("Время жизни токена истекло!");
            } catch (SignatureException e) {
                System.out.println("Неправильная подпись!");
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            ExtendUserDetails userDetails = new ExtendUserDetails(id,username, roles);
            JwtAuthentication token=new JwtAuthentication(userDetails,null);
            /*UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtTokenUtil.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));*/
            SecurityContextHolder.getContext().setAuthentication(token);

        }
        filterChain.doFilter(request, response);

    }
}