import java.math.BigDecimal;
import java.util.Objects;

public class MenuItem {
    private Integer menuId;
    private Category category;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imagePath;
    private boolean isAvailable;

    public MenuItem() {}

    public MenuItem(int menuId, Category category, String name, String description, BigDecimal price, int stock, String imagePath, boolean isAvailable) {
        this.menuId = menuId;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
        this.isAvailable = isAvailable;
    }

    public int getMenuId() {
        return menuId;
    }
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "[" + menuId + "] " + name + " - " + (category != null ? category.getCategoryName() : "NoCategory");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItem menuItem = (MenuItem) o;
        return menuId == menuItem.menuId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId);
    }
}
