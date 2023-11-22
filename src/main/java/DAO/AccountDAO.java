package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    public Account register(Account ac1)  {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
            preparedStatement.setString(1, ac1.getUsername());
            preparedStatement.setString(2, ac1.getPassword());
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                // Retrieve the generated keys (including the auto-incremented account_id)
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Set the generated account_id in the Account object
                    ac1.setAccount_id(generatedKeys.getInt("account_id"));
                    return ac1;
                }
            }
        } catch (SQLException e) {
            // Handle the exception or log it
            System.out.println(e.getMessage()); // Optionally rethrow the exception
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
    
    
    public Account login(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
    
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
    
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account acc = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return acc;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    
}
