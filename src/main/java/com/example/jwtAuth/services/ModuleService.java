package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.ModuleDAO;
import com.example.jwtAuth.models.Module;

import java.util.List;

public class ModuleService {
    private final ModuleDAO moduleDAO;

    public ModuleService(ModuleDAO moduleDAO) {
        this.moduleDAO = moduleDAO;
    }
    public List<Module> findByCourseId(Integer courseId){
        return moduleDAO.findByCourseId(courseId);
    }
}
