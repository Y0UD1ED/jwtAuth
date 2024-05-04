package com.example.jwtAuth.services;


import com.example.jwtAuth.dao.BonusDAO;
import com.example.jwtAuth.models.Bonus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonusesService {

    private final BonusDAO bonusesDAO;



    public BonusesService(BonusDAO bonusesDAO) {
        this.bonusesDAO = bonusesDAO;

    }


    public List<Bonus> getAllBonuses() {
        List<Bonus> bonuses = bonusesDAO.getAllBonuses();
        return bonuses;
    }

    public Bonus getBonusById(Integer bonusId) {
        return bonusesDAO.getBonusById(bonusId);
    }


    public void addBonus(Bonus bonuses) {
        bonusesDAO.addBonus(bonuses);
    }





}
