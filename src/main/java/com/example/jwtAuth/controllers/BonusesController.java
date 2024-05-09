package com.example.jwtAuth.controllers;


import com.example.jwtAuth.models.Bonus;
import com.example.jwtAuth.services.BonusPurchaseService;
import com.example.jwtAuth.services.BonusesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bonuses")
public class BonusesController {
    private final BonusesService bonusesService;

    private final BonusPurchaseService bonusPurchaseService;

    public BonusesController(BonusesService bonusesService, BonusPurchaseService bonusPurchaseService) {
        this.bonusesService = bonusesService;
        this.bonusPurchaseService = bonusPurchaseService;
    }

    @PostMapping("/add")
    public void addBonus(@RequestBody  Bonus bonuses) {
        bonusesService.addBonus(bonuses);
    }

    @GetMapping("/all")
    public List<Bonus> getAllBonuses() {
        //mapper
        return bonusesService.getAllBonuses();
    }

    @GetMapping("/{id}")
    public Bonus getBonus(@PathVariable("id") Integer id) {
        return bonusesService.getBonusById(id);
    }


    @PostMapping("/{id}/purchase")
    public void buyBonus(@PathVariable("id") int id) {
        bonusPurchaseService.purchaseBonus(id);
    }

    @GetMapping("/purchased")
    public List<Bonus> getPurchasedBonusesForUser() {
        return bonusPurchaseService.getPurchasedBonusesForUser();
    }


}
