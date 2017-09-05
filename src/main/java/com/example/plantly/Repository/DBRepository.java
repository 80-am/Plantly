package com.example.plantly.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// här vi kommer skriva alla som gäller koppling med databas och

@Component
public class DBRepository implements PlantyDBRepository {

    @SuppressWarnings("SpringJavaAutowiringInspection")

    @Autowired
    private DataSource dataSource;

    public  Plant getPlantByPlantGenus (String plantGenus){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT PlantGenus, PlantID FROM Plants WHERE Username = ?")){
            ps.setString(1, plantGenus);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Plant user = new Plant(rs.getString("PlantGenus"), rs.getString("Password"), rs.getInt("Userid"));
                    return user;
                }
            }catch(SQLException e){
                return null;
            }
        }catch(SQLException e){
            throw new PlantyRepositoryException("Connection in getUserByUserName failed!");
        }
        return null;
    }

    public void addPlantToUserPlants(String username, String password){
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO USERS(Username, Password) VALUES(?,?)", new String[]{"UserID"})) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }


}
