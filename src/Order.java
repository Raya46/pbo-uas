import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class Order {

    private int orderId;
    private int customerId;
    private BigDecimal totalPrice;
    private Date orderDate;

    // Constructor untuk load dari database
    public Order(int orderId, int customerId, BigDecimal totalPrice, Timestamp timestamp) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.orderDate = new Date(timestamp.getTime());
    }

    // Constructor untuk membuat order baru
    public Order(int customerId, BigDecimal totalPrice, Date orderDate) {
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // GETTERS & SETTERS
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}
