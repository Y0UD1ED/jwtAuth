package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.InfoModule;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class InfoModuleDAO {
    private final JdbcTemplate jdbcTemplate;

    public InfoModuleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

   public List<InfoModule> findByModuleId(Integer moduleId){
        String sql="SELECT * FROM info_modules WHERE module_id=(?)";
        return jdbcTemplate.query(sql,(rs, rowNum) -> {
            InfoModule infoModule= new InfoModule();
            infoModule.setInfoId(rs.getInt("id"));
            infoModule.setModuleId(moduleId);
            infoModule.setContent(rs.getString("content"));
            infoModule.setContentType(rs.getString("type"));
            infoModule.setContentPosition(rs.getInt("order"));
            return infoModule;
        }).stream().toList();
   }

   public void delete(Integer moduleId){
        String sql="DELETE FROM info_module WHERE module_id=(?)";
        jdbcTemplate.update(sql,moduleId);
   }
}
