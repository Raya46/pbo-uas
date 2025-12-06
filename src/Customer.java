public class Customer {
    private int customerId;
    private String username;
    private String password;
    private String fullName;

    public Customer() {}

    public Customer(int customerId, String username, String password, String fullName) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}