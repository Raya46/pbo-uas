import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class Order {

    private int orderId;
    private int userId;
    private int tableId;
    private BigDecimal totalAmount;
    private Date orderDate;
    private String status;
    private String paymentMethod;

    // Constructor untuk load dari database
    public Order(int orderId, int userId, BigDecimal totalAmount, Timestamp timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderDate = new Date(timestamp.getTime());
    }

    // Constructor untuk membuat order baru
    public Order(int userId, BigDecimal totalAmount, Date orderDate) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = "pending";
        this.paymentMethod = "cash";
    }

    // GETTERS & SETTERS
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    // untuk compatibility dengan CustomerOrderHistoryFrame
    public int getId() { return orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    // Method untuk compatibility dengan kode lama
    public int getCustomerId() { return userId; }
    public void setCustomerId(int customerId) { this.userId = customerId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public BigDecimal getTotalPrice() { return totalAmount; } // untuk compatibility
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalAmount = totalPrice; } // untuk compatibility
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
