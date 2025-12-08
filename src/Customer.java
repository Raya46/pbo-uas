// DEPRECATED: Class ini tidak digunakan lagi
// Semua user (admin, kasir, customer) menggunakan class User
public class Customer extends User {
    
    public Customer() {
        super();
        setRole("customer");
    }
    
    public Customer(int userId, String username, String password, String fullName) {
        super(userId, username, password, fullName, "customer");
    }
    
    // Method untuk compatibility dengan kode lama
    public int getCustomerId() { return getUserId(); }
    public void setCustomerId(int customerId) { setUserId(customerId); }
}
