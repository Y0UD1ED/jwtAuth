package com.example.jwtAuth.controllers;


import com.example.jwtAuth.models.Bonus;
import com.example.jwtAuth.services.BonusesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bonuses")
public class BonusesController {
    private final BonusesService bonusesService;

    public BonusesController(BonusesService bonusesService) {
        this.bonusesService = bonusesService;
    }

    @PostMapping("/add")
    public void addBonus(@RequestBody  Bonus bonuses) {
        bonusesService.addBonus(bonuses);
    }

    @GetMapping("/get")
    public List<Bonus> getAllBonuses() {
        return bonusesService.getAllBonuses();
    }

    @PostMapping("/{id}/buy")
    public void buyBonus(@PathVariable("id") int id) {
        bonusesService.buyBonus(id);
    }

    @GetMapping("/my")
    public List<Bonus> getMyBonuses() {
        return bonusesService.getMyBonuses();
    }

}