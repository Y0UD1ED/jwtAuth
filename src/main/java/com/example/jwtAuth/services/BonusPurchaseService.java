package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.BonusDAO;
import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.models.Bonus;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonusPurchaseService {
    private final UserDAO userDAO;
    private final BonusDAO bonusDAO;

    public BonusPurchaseService(UserDAO userDAO, BonusDAO bonusDAO) {
        this.userDAO = userDAO;
        this.bonusDAO = bonusDAO;
    }
    public void purchaseBonus(int bonusId) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        User user=userDAO.getUserById(userId);
        Bonus bonus = bonusDAO.getBonusById(bonusId);
        Integer price=bonus.getPrice();
        Integer balance=user.getBalance();
        if(balance<price) {
            throw new RuntimeException("Not enough money");
        }
        user.setBalance(balance-price);
        userDAO.updateUserBalance(userId,user.getBalance());
        bonusDAO.addBonusForUser(userId,bonusId);
    }

    public List<Bonus> getPurchasedBonusesForUser() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        List<Bonus> bonuses = bonusDAO.getUserBonuses(userId);
        return bonuses;
    }

}
