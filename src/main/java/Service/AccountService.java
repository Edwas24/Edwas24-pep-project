package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) throws SQLException {
        this.accountDAO = accountDAO;
    }

    public Account register(Account ac1) {
    
    
        String username = ac1.getUsername();
        String password = ac1.getPassword();
        if (username.isEmpty()) {
            return null;
        }
        if (password.length() < 4) {
            return null;
        }
        if (!isUsernameTaken(username, password)) {
            return accountDAO.register(ac1);
        }
   
    

    return null;
}

    private boolean isUsernameTaken(String username, String password) {
        // Check if the username already exists
        return accountDAO.login(username, password) != null;
    }

    public Account login(String username, String password) {
        
        return accountDAO.login(username, password);
    }
    
}
