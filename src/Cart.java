import java.util.ArrayList;

public class Cart {

    private ArrayList<CartItem> items = new ArrayList<>();

    public void addItem(MenuItem menuItem, int qty) {
        for (CartItem item : items) {
            if (item.getMenuItem().getMenuId() == menuItem.getMenuId()) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }
        items.add(new CartItem(menuItem, qty));
    }

    public void removeItem(int menuId) {
        items.removeIf(item -> item.getMenuItem().getMenuId() == menuId);
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clear() {
        items.clear();
    }
}
