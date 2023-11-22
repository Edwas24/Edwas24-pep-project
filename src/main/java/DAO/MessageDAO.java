package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
public class MessageDAO {
    
    public Message createM(Message m1) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
            preparedStatement.setInt(1, m1.getPosted_by());
            preparedStatement.setString(2, m1.getMessage_text());
            preparedStatement.setLong(3, m1.getTime_posted_epoch());
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                // Retrieve the generated keys (including the auto-incremented message_id)
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Set the generated message_id in the Message object
                    m1.setMessage_id(generatedKeys.getInt("message_id"));
                    return m1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
    

    
    public void deleteM(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
    
            preparedStatement.setInt(1, message_id);
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("Message deleted successfully");
            } else {
                System.out.println("Message not found or deletion failed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public List<Message> getAllmess() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> msgs = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
    
            while (rs.next()) {
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                msgs.add(msg);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return msgs; // Always return the list, even if it's empty
    }
    
    
    public List<Message> getAllMessagesForAccount(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
    
            while (rs.next()) {
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(msg);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return messages;
    }
    
    
    public Message getmsgByID(int message_id ){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "SELECT * from message where message_id  = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setInt method here.
            preparedStatement.setInt(1, message_id  );
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                return msg;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void updateM(int messageId, String updatedMessageText) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Write SQL logic here
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
    
            // Set the parameters for the prepared statement
            preparedStatement.setString(1, updatedMessageText);
            preparedStatement.setInt(2, messageId);
    
            // Execute the update to modify the message text
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("Message updated successfully");
            } else {
                System.out.println("Message not found or update failed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    

}
