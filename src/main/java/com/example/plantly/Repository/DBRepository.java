package com.example.plantly.Repository;

import com.example.plantly.Domain.Plant;
import com.example.plantly.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// här vi kommer skriva alla som gäller koppling med databas och

@Component
public class DBRepository implements PlantyDBRepository {

    //@SuppressWarnings("SpringJavaAutowiringInspection")

    @Autowired
    private DataSource dataSource;
    
   @Override
    public Plant getPlantByPlantSpecies (String plantSpecies){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Plants WHERE PlantSpecies = ?")){
            ps.setString(1, plantSpecies);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Plant plant = new Plant(rs.getString("PlantSpecies"),
                                            rs.getString("PlantGenus"),
                                            rs.getString("PlantInfo"),
                                            rs.getString("Water"),
                                            rs.getString("Tempature"),
                                            rs.getString("Humidity"),
                                            rs.getString("Flowering"),
                                            rs.getString("Pests"),
                                            rs.getString("Diseases"),
                                            rs.getString("Soil"),
                                            rs.getString("PotSize"),
                                            rs.getInt("Poisonous"),
                                            rs.getInt("DaysUntilWatering"),
                                            rs.getString("Fertilizer"),
                                            rs.getString("Light"),
                                            rs.getInt("plantID"));
                    return plant;
                }
            }catch(SQLException e){
                return null;
            }
        }catch(SQLException e){
            throw new PlantyRepositoryException("Connection in getPlantByPlantSpecies failed!");
        }
        return null;
    }

    @Override
    public String addPlantToUserPlants(String nickName, String photo, int userId, String plantSpecies){
        int plantId = getPlantIdFromPlants(plantSpecies);
        if(plantId != 0){
            try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO UsersPlants(UserID, NickName, Photo, PlantID) VALUES(?,?,?,?)")) {
                ps.setInt(1, userId);
                ps.setString(2, nickName);
                ps.setString(3, photo);
                ps.setInt(4, plantId);
                ps.executeUpdate();
            } catch (SQLException e) {
            }
            return "Success!";
        }
        return "Fail!";
    }

    @Override
    public boolean addUser(String email, String firstname, String lastname, String password) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [Users] (Email, FirstName, LastName, Password) values (?,?,?,?) ")) {
            ps.setString(1, email);
            ps.setString(2, firstname);
            ps.setString(3, lastname);
            ps.setString(4, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
        }
        return false;
    }

    public List<User> getAllUsers() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM [Users]")) {
            List<User> users = new ArrayList<>();
            while (rs.next()) users.add(rsUser(rs));
            return users;
        } catch (SQLException e) {
            throw new PlantyRepositoryException(e);
        }
    }

    private User rsUser(ResultSet rs) throws SQLException {
        return new User(rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Email"), rs.getString("Password"));
    }

    public int getPlantIdFromPlants(String plantSpecies){
        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT PlantID FROM Plants WHERE PlantSpecies = ?")) {
            ps.setString(1, plantSpecies);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int plantId = rs.getInt("plantID");
                    return plantId;
                }
            }catch(SQLException e){
                return 0;
            }
        }catch (SQLException e){
            throw new PlantyRepositoryException(e);
        }
        return 0;
    }
}
