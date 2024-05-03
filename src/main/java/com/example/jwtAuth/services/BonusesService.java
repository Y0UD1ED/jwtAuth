package com.example.jwtAuth.services;


import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.BonusDAO;
import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.dao.UsersBonusesDAO;
import com.example.jwtAuth.models.Bonus;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.UsersBonuses;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BonusesService {

    private final BonusDAO bonusesDAO;

    private final UserDAO userDAO;
    private final UsersBonusesDAO usersBonusesDAO;


    public BonusesService(BonusDAO bonusesDAO, UsersBonusesDAO usersBonusesDAO, UserDAO userDAO) {
        this.bonusesDAO = bonusesDAO;
        this.usersBonusesDAO = usersBonusesDAO;
        this.userDAO = userDAO;
    }

    public List<Bonus> getAllBonusesShort() {
        return bonusesDAO.getAllBonuses();
    }

    public List<Bonus> getAllBonuses() {
        List<Bonus> bonuses = bonusesDAO.getAllBonuses();
        return bonuses;
    }

    private boolean isFirstBonus(int bonusId, int userId) {
        return !usersBonusesDAO.check(userId, bonusId);
    }

    public void buyBonus(int bonusId) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        Integer price=bonusesDAO.getBonusPrice(bonusId);
        Integer balance=userDAO.getUserBalance(userId);
        if(balance<price) {
            throw new RuntimeException("Not enough money");
        }
        userDAO.updateUserBalance(userId,balance-price);
        Boolean isFirst = isFirstBonus(bonusId, userId);
        if(isFirst) {
            usersBonusesDAO.setBonus(userId, bonusId,1);
        } else {
            usersBonusesDAO.updateBonus(userId, bonusId, 1);
        }
    }

    public List<UsersBonuses> getAllBonusesByUserId(Integer userId) {
        return usersBonusesDAO.show(userId);
    }

    public void addBonus(Bonus bonuses) {
        bonusesDAO.addBonus(bonuses);
    }





}
