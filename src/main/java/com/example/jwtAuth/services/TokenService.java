package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.TokenDAO;
import com.example.jwtAuth.dtos.JwtResponse;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.Role;
import com.example.jwtAuth.models.Token;
import com.example.jwtAuth.utils.JwtTokenUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {
    private final TokenDAO tokenDAO;
    private final JwtTokenUtil jwtTokenUtil;

    public TokenService(TokenDAO tokenDAO, JwtTokenUtil jwtTokenUtil) {
        this.tokenDAO = tokenDAO;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String generateAccessToken(ExtendUserDetails user) {
        String accesstoken = jwtTokenUtil.generateToken(user);
        return accesstoken;

    }

    public String generateRefreshToken(ExtendUserDetails user) {
        String refreshtoken = jwtTokenUtil.generateRefreshToken(user);

        Token token = tokenDAO.show(user.getId());
        if (token != null) {
            token.setToken(refreshtoken);
            tokenDAO.update(token);
        } else {
            Token newToken = new Token();
            newToken.setUser_id(user.getId());
            newToken.setToken(refreshtoken);
            tokenDAO.save(newToken);
        }
        return refreshtoken;
    }

    public String generateAccessToken(String refreshtoken) {
        String accessToken = null;
        ExtendUserDetails user = getUserFromToken(refreshtoken);
        Token savedToken=tokenDAO.show(user.getId());
        if (savedToken != null && savedToken.getToken().equals(refreshtoken)) {
           accessToken = jwtTokenUtil.generateToken(user);
        }
        return accessToken;
    }

    private ExtendUserDetails getUserFromToken(String token) {
        List<Role> roles = jwtTokenUtil.getRoles(token);
        String username = jwtTokenUtil.getUsername(token);
        Integer id = jwtTokenUtil.getId(token);
        return new ExtendUserDetails(id, username, roles);
    }

    public void deleteToken(Integer id) {
        tokenDAO.delete(id);
    }
}