public class MenuItem {
    private int menuId;
    private String name;
    private String category;
    private double price;
    private int stock;

    public MenuItem() {}

    public MenuItem(int menuId, String name, double price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public MenuItem(int menuId, String name, String category, double price, int stock) {
        this.menuId = menuId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}