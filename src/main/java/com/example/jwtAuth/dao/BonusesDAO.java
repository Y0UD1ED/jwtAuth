package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Bonuses;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BonusesDAO {
    private final JdbcTemplate jdbcTemplate;

    public BonusesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
   public List<Bonuses> findAllBonuses(){
        String sql = "SELECT * FROM bonuses";
        return jdbcTemplate.query(sql, (rs,rowNum)->{
          Bonuses bonuses = new Bonuses();
          bonuses.setId(rs.getInt("id"));
          bonuses.setDescription(rs.getString("description"));
          bonuses.setName(rs.getString("name"));
          bonuses.setPrice(rs.getInt("price"));
          bonuses.setCount(rs.getInt("count"));
          return bonuses;
        });
   }

   public void addBonuses(Bonuses bonuses){
        String sql = "INSERT INTO bonuses (description,name,price,count) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, bonuses.getDescription(), bonuses.getName(), bonuses.getPrice(), bonuses.getCount());
   }

   public void updateBonuses(Bonuses bonuses){
        String sql = "UPDATE bonuses SET description =?, name =?, price =?, count =? WHERE id =?";
        jdbcTemplate.update(sql, bonuses.getDescription(), bonuses.getName(), bonuses.getPrice(), bonuses.getCount(), bonuses.getId());
   }

   public void deleteBonuses(int id){
        String sql = "DELETE FROM bonuses WHERE id =?";
        jdbcTemplate.update(sql, id);
   }


}
