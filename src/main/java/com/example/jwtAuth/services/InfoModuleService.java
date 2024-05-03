package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.InfoModuleDAO;
import com.example.jwtAuth.models.InfoModule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoModuleService {
    private final InfoModuleDAO infoModuleDAO;

    public InfoModuleService(InfoModuleDAO infoModuleDAO) {
        this.infoModuleDAO = infoModuleDAO;
    }

    public List<InfoModule> findByCourseId(Integer moduleId){
        return infoModuleDAO.getByCourseId(moduleId);
    }
    public void save(List<InfoModule> infoModule){
        for(InfoModule infoModule1:infoModule){
            infoModuleDAO.addInfoModule(infoModule1);
        }
    }

    public void saveOneInfoModule(InfoModule infoModule){
        infoModuleDAO.addInfoModule(infoModule);
    }
}
