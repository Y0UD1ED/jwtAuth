package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Property;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PropertyDAO {

    private final JdbcTemplate jdbcTemplate;

    public PropertyDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Property> getAllPropertiesObject(Integer tovarId) {
        String sql="SELECT * FROM tovar_properties JOIN properties ON tovar_properties.property_id = properties.id WHERE tovar_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Property property = new Property();
            property.setId(rs.getInt("id"));
            property.setPropertyId(rs.getInt("property_id"));
            property.setPropertyName(rs.getString("name"));
            property.setPropertyValue(rs.getString("value"));
            return property;
        },tovarId);
    }

    public void addPropertyForObject(Integer tovarId, Property property) {
        String sql="INSERT INTO tovar_properties (tovar_id, property_id, value) VALUES (?,?,?)";
        jdbcTemplate.update(sql, tovarId, property.getPropertyId(), property.getPropertyValue());
    }

    public void addProperty(String name) {
        String sql="INSERT INTO properties (name) VALUES (?)";
        jdbcTemplate.update(sql, name);
    }

    public void deleteProperty(Integer propertyId) {
        String sql="DELETE FROM properties WHERE id=?";
        jdbcTemplate.update(sql, propertyId);
    }

    public void deletePropertiesForObject(Integer tovarId) {
        String sql="DELETE FROM tovar_properties WHERE tovar_id=?";
        jdbcTemplate.update(sql, tovarId);
    }

    public void updateProperty(Integer tovarId, Property property) {
        String sql="UPDATE properties SET value=? WHERE tovar_id=? AND property_id=?";
        jdbcTemplate.update(sql, property.getPropertyValue(), tovarId, property.getPropertyId());
    }


}
