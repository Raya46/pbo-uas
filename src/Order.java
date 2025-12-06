import java.util.Date;

public class Order {
    private int id;
    private int customerId;
    private double totalPrice;
    private Date orderDate;

    public Order(int id, int customerId, double totalPrice, Date orderDate) {
        this.id = id;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public Order(int customerId, double totalPrice, Date orderDate) {
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public double getTotalPrice() { return totalPrice; }
    public Date getOrderDate() { return orderDate; }
}
