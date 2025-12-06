public class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private int menuId;
    private int quantity;
    private double price;

    public OrderDetail(int orderId, int menuId, int quantity, double price) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderDetail(int orderDetailId, int orderId, int menuId, int quantity, double price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderDetailId() { return orderDetailId; }
    public int getOrderId() { return orderId; }
    public int getMenuId() { return menuId; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}