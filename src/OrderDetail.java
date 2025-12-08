import java.math.BigDecimal;

public class OrderDetail {

    private int orderDetailId;
    private int orderId;
    private int menuId;
    private int quantity;
    private BigDecimal price;

    // === Constructor utama (dipakai saat insert order detail) ===
    public OrderDetail(int orderId, int menuId, int quantity, BigDecimal price) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
    }

    // === Optional: constructor lengkap (untuk load dari DB) ===
    public OrderDetail(int orderDetailId, int orderId, int menuId, int quantity, BigDecimal price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
    }

    // === Getters & Setters ===
    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
