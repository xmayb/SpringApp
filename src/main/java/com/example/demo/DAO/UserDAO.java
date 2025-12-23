package com.example.demo.DAO;

import com.example.demo.Entity.User;
import org.springframework.stereotype.Repository;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

@Repository
public class UserDAO {
    String sqlCreate = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
    String sqlDelete = "DELETE FROM users WHERE id = ?";
    String sqlUpdate = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
    String sqlRead = "SELECT * FROM users WHERE id = ?";
    String url = "jdbc:postgresql://localhost:5432/taskflow";
    String dbUser = "postgres";
    String dbPassword = "fifa6744";

    //Create
    public void createUser(User user) {

        try (Connection connection = getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = connection.prepareStatement(sqlCreate)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " rows were updated.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Read
    public User getUserById(Long id) {
        User user = null;
        try(Connection conn = getConnection(url, dbUser, dbPassword);
            PreparedStatement pstmt = conn.prepareStatement(sqlRead)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                user = new User(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"));
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    //UPdate
    public void updateUser(User user) {
        try(Connection conn = getConnection(url, dbUser, dbPassword);
            PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setLong(4, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Delete
    public void deleteUser(Long id) {
        try(Connection connection = getConnection(url, dbUser, dbPassword);
            PreparedStatement preparedStat = connection.prepareStatement(sqlDelete)){
            preparedStat.setLong(1, id);
            preparedStat.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
