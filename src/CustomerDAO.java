import java.sql.*;

public class CustomerDAO {

    public Customer login(String username, String password) {
        Customer customer = null;
        String sql = "SELECT * FROM customers WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}