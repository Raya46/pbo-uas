import java.math.BigDecimal;

public class CartItem {
    private MenuItem menuItem;
    private int quantity;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    // Getter untuk object MenuItem
    public MenuItem getMenuItem() { return menuItem; }

    // Getter Setter Quantity
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Menghitung subtotal (Harga Menu * Jumlah)
    public double getTotalPrice() {
        // ERROR FIX: Menggunakan .multiply() untuk BigDecimal
        // Kita konversi quantity (int) menjadi BigDecimal, kalikan, lalu kembalikan sebagai double
        BigDecimal price = menuItem.getPrice();
        return price.multiply(BigDecimal.valueOf(quantity)).doubleValue();
    }
}