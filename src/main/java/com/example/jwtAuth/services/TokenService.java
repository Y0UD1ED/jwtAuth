package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.TokenDAO;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.Role;
import com.example.jwtAuth.models.Token;
import com.example.jwtAuth.utils.JwtTokenUtil;
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
        boolean check= tokenDAO.isTokenExist(user.getId());
        if (check) {
            Token token = tokenDAO.getTokenByUserId(user.getId());
            token.setToken(refreshtoken);
            tokenDAO.updateToken(token);
        } else {
            Token newToken = new Token();
            newToken.setUser_id(user.getId());
            newToken.setToken(refreshtoken);
            tokenDAO.addToken(newToken);
        }
        return refreshtoken;
    }

    public String generateAccessToken(String refreshtoken) {
        String accessToken = null;
        ExtendUserDetails user = getUserFromToken(refreshtoken);
        boolean check= tokenDAO.isTokenExist(user.getId());
        Token savedToken=tokenDAO.getTokenByUserId(user.getId());
        if (check && savedToken.getToken().equals(refreshtoken)) {
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
        tokenDAO.deleteToken(id);
    }
}