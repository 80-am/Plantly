package com.example.plantly.Repository;

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

    @SuppressWarnings("SpringJavaAutowiringInspection")

    @Autowired
    private DataSource dataSource;

/*
    @Override
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
     
    @Override
    public void addPlantToUserPlants(String username, String password){
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO USERS(Username, Password) VALUES(?,?)", new String[]{"UserID"})) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    */

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
}
