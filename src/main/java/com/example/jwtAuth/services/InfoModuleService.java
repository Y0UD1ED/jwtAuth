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

    public InfoModule findByCourseId(Integer moduleId){
        return infoModuleDAO.getByCourseId(moduleId);
    }
    public void save(InfoModule infoModule,Integer courseId){
        infoModuleDAO.addInfoModule(infoModule,courseId);

    }

}
