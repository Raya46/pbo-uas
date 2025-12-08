import java.sql.*;

// DEPRECATED: Class ini tidak digunakan lagi
// Gunakan UserDAO untuk semua operasi user termasuk customer
public class CustomerDAO {

    private UserDAO userDAO;
    
    public CustomerDAO() {
        this.userDAO = new UserDAO();
    }

    public Customer login(String username, String password) {
        // Gunakan UserDAO untuk login customer
        User user = userDAO.validateLogin(username, password);
        if (user != null && user.getRole().equalsIgnoreCase("customer")) {
            return new Customer(user.getUserId(), user.getUsername(), user.getPassword(), user.getFullName());
        }
        return null;
    }
    
    public Customer getCustomerById(int customerId) {
        User user = userDAO.findById(customerId);
        if (user != null && user.getRole().equalsIgnoreCase("customer")) {
            return new Customer(user.getUserId(), user.getUsername(), user.getPassword(), user.getFullName());
        }
        return null;
    }
    
    public boolean addCustomer(Customer customer) {
        User user = new User();
        user.setUsername(customer.getUsername());
        user.setPassword(customer.getPassword());
        user.setFullName(customer.getFullName());
        user.setRole("customer");
        
        int result = userDAO.insert(user);
        return result > 0;
    }
}