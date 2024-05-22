package com.example.jwtAuth.controllers;

import com.example.jwtAuth.exceptions.AppError;
import com.example.jwtAuth.services.PassingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.Map;

@RestController
public class PassingController {
    private final PassingService passingService;

    public PassingController(PassingService passingService) {
        this.passingService = passingService;
    }
    @PostMapping("/questModule/{id}/pass")
    public ResponseEntity<?> passQuestModule(@PathVariable(value = "id") Integer id, @RequestBody Map<Integer,Integer> answers) {
        try{
            return ResponseEntity.ok(passingService.passQuestModule(id,answers));
        }catch (AuthenticationException e){
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),e.getMessage()),HttpStatus.FORBIDDEN);
        }

    }
}
