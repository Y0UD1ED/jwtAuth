package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Bonus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BonusDAO {
    private final JdbcTemplate jdbcTemplate;

    public BonusDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
   public List<Bonus> getAllBonuses(){
        String sql = "SELECT * FROM bonuses";
        return jdbcTemplate.query(sql, (rs,rowNum)->{
          Bonus bonus = new Bonus();
          bonus.setId(rs.getInt("id"));
          bonus.setDescription(rs.getString("description"));
          bonus.setName(rs.getString("name"));
          bonus.setPrice(rs.getInt("price"));
          bonus.setCount(rs.getInt("count"));
          bonus.setImage(rs.getString("image"));
          return bonus;
        });
   }

   public Bonus getBonusById(Integer id){
        String sql = "SELECT * FROM bonuses WHERE id =?";
        return jdbcTemplate.queryForObject(sql, (rs,rowNum)->{
          Bonus bonus = new Bonus();
          bonus.setId(rs.getInt("id"));
          bonus.setDescription(rs.getString("description"));
          bonus.setName(rs.getString("name"));
          bonus.setPrice(rs.getInt("price"));
          bonus.setCount(rs.getInt("count"));
          bonus.setImage(rs.getString("image"));
          return bonus;
        }, id);
   }
    public List<Bonus> getUserBonuses(Integer userId) {
        String sql = "SELECT bonuses.id,bonuses.name,bonuses.description,bonuses.price,users_bonuses.count " +
                "FROM users_bonuses\n" +
                "JOIN bonuses ON users_bonuses.bonus_id=bonuses.id\n" +
                "WHERE users_bonuses.user_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Bonus bonus = new Bonus();
            bonus.setId(rs.getInt("id"));
            bonus.setName(rs.getString("name"));
            bonus.setDescription(rs.getString("description"));
            bonus.setPrice(rs.getInt("price"));
            bonus.setCount(rs.getInt("count"));
            return bonus;
        }, userId);
    }
    public void addBonusForUser(Integer userId, int bonusId) {
        String sql="INSERT INTO users_bonuses (user_id,bonus_id,count) VALUES (?,?,?)";
        jdbcTemplate.update(sql,userId,bonusId,1);
    }
   public void addBonus(Bonus bonus){
        String sql = "INSERT INTO bonuses (description,name,price,count) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, bonus.getDescription(), bonus.getName(), bonus.getPrice(), bonus.getCount());
   }

   public void updateBonus(Bonus bonus){
        String sql = "UPDATE bonuses SET description =?, name =?, price =?, count =? WHERE id =?";
        jdbcTemplate.update(sql, bonus.getDescription(), bonus.getName(), bonus.getPrice(), bonus.getCount(), bonus.getId());
   }

   public void deleteBonus(int id){
        String sql = "DELETE FROM bonuses WHERE id =?";
        jdbcTemplate.update(sql, id);
   }


}
