package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    
    public MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    
        

    public Message createMessage(Message message) {
        // Gets the posted id
        int postedById = message.getPosted_by();
    
        // Check if the user exists
        if (messageDAO.getmsgByID(postedById) == null) {
            return null; // User does not exist
        }
    
        // Check if the message text is not empty and its length is greater than 0 and less than or equal to 255
        if (message.getMessage_text() != null && !message.getMessage_text().isEmpty() &&
                message.getMessage_text().length() > 0 && message.getMessage_text().length() <= 255) {
            // Create and return the message
            return messageDAO.createM(message);
        }
    
        return null; // Invalid message text
    }
    
    public Message deleteMessageById(int messageId) {
        // Check if the message with the given ID exists
        Message existingMessage = messageDAO.getmsgByID(messageId);
        if (existingMessage == null) {
            return null; // Message not found
        }

        // Delete the message
        messageDAO.deleteM(messageId);
        return existingMessage;
    }
    public List<Message> getAllMessagesForAccount(int accountId) {
        // Call your DAO method to get all messages for the account
        return messageDAO.getAllMessagesForAccount(accountId);
    }
    public List<Message> getAllMessages() {
        // Call your DAO method to get all messages for the account
        return messageDAO.getAllmess();
    }
    
    public Message updateMessage(int messageId, String updatedMessageText) {
        // Check if the message with the given ID exists
        Message existingMessage = messageDAO.getmsgByID(messageId);
        if (existingMessage == null) {
            return null; // Message not found
        }
    
        if (updatedMessageText != null && !updatedMessageText.isEmpty() && updatedMessageText.length() <= 255) {
            // Update the message text
            messageDAO.updateM(messageId, updatedMessageText);
    
            // Return the updated message
            return new Message(
                    existingMessage.getMessage_id(),
                    existingMessage.getPosted_by(),
                    updatedMessageText,
                    existingMessage.getTime_posted_epoch()
            );
        }
    
        return null;
    }
    
    
    public Message retrieveMessage(int messageId){
        return messageDAO.getmsgByID(messageId);

    }

}
