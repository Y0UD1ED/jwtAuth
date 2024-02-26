package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.InfoModuleDAO;
import com.example.jwtAuth.models.InfoModule;

import java.util.List;

public class InfoModuleService {
    private final InfoModuleDAO infoModuleDAO;

    public InfoModuleService(InfoModuleDAO infoModuleDAO) {
        this.infoModuleDAO = infoModuleDAO;
    }

    public List<InfoModule> findByModuleId(Integer moduleId){
        return infoModuleDAO.findByModuleId(moduleId);
    }
}
