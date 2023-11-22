package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     AccountService accountService;
     MessageService messageService;
     List<Message> em;
     public SocialMediaController(){
         this.messageService = new MessageService();
         this.accountService = new AccountService();
     }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
       // app.get("example-endpoint", this::exampleHandler);
        app.post("/messages", this::MsgHandler);
        app.delete("/messages/{id}", this::deleteMessageHandler);
       app.get("/accounts/{accountId}/messages", this::getAllMessagesHandler);
       app.get("/messages", this::getAllMessHandler);

        app.get("/messages/{id}", this::getMessageByIdHandler);
      
        app.patch("/messages/{id}", ctx -> updateMessageHandler(ctx));
        app.post("/register", this::RegHandler);
        app.post("/login", this::LoginHandler);  // Added leading slash
        

 

        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    //private void exampleHandler(Context context) {
      //  context.json("sample text");
    //}
     
    private void RegHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account1 = mapper.readValue(ctx.body(), Account.class);
        Account registerdAccount = accountService.register(account1);
        if(registerdAccount!=null){
            ctx.json(mapper.writeValueAsString(registerdAccount));
        }else{
            ctx.status(400);
        }
    } 
    private void LoginHandler(Context ctx) {
        try {
            String requestBody = ctx.body();
            if (requestBody != null && !requestBody.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                String u = mapper.readTree(requestBody).get("username").asText();
                String p = mapper.readTree(requestBody).get("password").asText();
    
        
                Account authenticatedUser = accountService.login(u, p);
    
                if (authenticatedUser != null) {
                
                    ctx.status(200); 
                    ctx.json(authenticatedUser);
                } else {
                    
                    ctx.status(401); 
                    ctx.json("");
                }
            } else {
                ctx.status(400).json("Request body is empty");
            }
        } catch (JsonProcessingException e) {
            
            ctx.status(400); 
            ctx.json("Error processing JSON");
        }
    }
    
    
   
  private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
    String accountIdStr = ctx.pathParam("accountId");

    if (accountIdStr != null) {
        try {
            int accountId = Integer.parseInt(accountIdStr);
            
            List<Message> messages = messageService.getAllMessagesForAccount(accountId);

            if (messages.isEmpty()) {
                ctx.status(200);
                ctx.json(Collections.emptyList()); 
            } else {
                ctx.json(messages);
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.json("Invalid 'accountId' parameter: " + accountIdStr);
        }
    } else {
        ctx.status(400);
        ctx.json("Missing 'accountId' parameter");
    }
}





private void getAllMessHandler(Context ctx) throws JsonProcessingException {
    
    List<Message> messages = messageService.getAllMessages();

    // If the list is empty, return an empty response body with status 400
    
        // If the list is not empty, return the list in the response body
        ctx.json(messages);
    
}


    
    private void getMessageByIdHandler(Context ctx) {
        String messageIdStr = ctx.pathParam("id");
    
        if (messageIdStr != null) {
            try {
                int messageId = Integer.parseInt(messageIdStr);
    
                // Call your message service to retrieve the message by ID
                Message retrievedMessage = messageService.retrieveMessage(messageId);
    
                if (retrievedMessage != null) {
                    // Message was successfully retrieved
                    ctx.json(retrievedMessage);
                } else {
                    // Message not found
                    ctx.status(200); 
                }
            } catch (NumberFormatException e) {
                // Handle the case where "id" is not a valid integer
                ctx.status(400); // 400 Bad Request
                ctx.json("Invalid 'id' parameter: " + messageIdStr);
            }
        } else {
            // Handle the case where "id" is not present
            ctx.status(400); // 400 Bad Request
            ctx.json("Missing 'id' parameter");
        }
    }
    
    private void MsgHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = ctx.body();
        
        if (requestBody != null && !requestBody.isEmpty()) {
            Message mes = mapper.readValue(requestBody, Message.class);
            System.out.println("Received message: " + mes);
    
            Message addedMessage = messageService.createMessage(mes);
            System.out.println("Added message: " + addedMessage);
            if (addedMessage != null) {
                ctx.status(200).json(mapper.writeValueAsString(addedMessage));
            } else {
                ctx.status(400);
            }
        } else {
            ctx.status(400).json("Request body is empty");
        }
    }
    
    private void deleteMessageHandler(Context ctx) {
        String messageIdStr = ctx.pathParam("id");
    
        if (messageIdStr != null) {
            try {
                int messageId = Integer.parseInt(messageIdStr);
    
                // Call your message service to delete the message by ID
                Message deletedMessage = messageService.deleteMessageById(messageId);
    
                if (deletedMessage != null) {
                    // Message was successfully deleted
                    ctx.json(deletedMessage); // Return the deleted message as JSON
                    ctx.status(200);
                } else {
                    // Message not found, or there was an issue with deletion
                    //ctx.json("{}"); // Return an empty JSON object
                    ctx.status(200);
                }
            } catch (NumberFormatException e) {
                // Handle the case where "id" is not a valid integer
                ctx.status(400);
            }
        } else {
            // Handle the case where "id" is not present
            ctx.status(400);
        }
    } 
    private void updateMessageHandler(Context ctx) {
        try {
            // Extract message ID from the path parameter
            String messageIdStr = ctx.pathParam("id");
            int messageId = Integer.parseInt(messageIdStr);
    
            // Extract the request body
            String requestBody = ctx.body();
            if (requestBody != null && !requestBody.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                String updatedMessageText = mapper.readTree(requestBody).get("message_text").asText();
    
                // Call the updateMessage method in your MessageService
                Message updatedMessage = messageService.updateMessage(messageId, updatedMessageText);
    
                if (updatedMessage != null) {
                    // Message was successfully updated
                    ctx.status(200); // OK
                    ctx.json(updatedMessage);
                } else {
                    // Message not found, or there was an issue with the update
                    ctx.status(400); // Bad Request
                    ctx.result("");
                }
            } else {
                ctx.status(400).json("Request body is empty");
            }
        } catch (Exception e) {
            // Handle other exceptions
            ctx.status(400); // Bad Request
            ctx.json("Invalid request format");
        }
    }
    

    
}
    
    
   