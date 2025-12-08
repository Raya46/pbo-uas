import java.sql.*;
import java.util.ArrayList;

public class MenuDAO {

    // contoh minimal
    public static int countMenu() {
        String sql = "SELECT COUNT(*) FROM menu_items";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public ArrayList<MenuItem> getAllMenuItems() {
        ArrayList<MenuItem> menuList = new ArrayList<>();
        String sql = "SELECT * FROM menu_items WHERE is_available = 1 ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setMenuId(rs.getInt("menu_id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setStock(rs.getInt("stock"));
                item.setImagePath(rs.getString("image_path"));
                item.setAvailable(rs.getBoolean("is_available"));
                
                // Set category jika diperlukan
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                item.setCategory(category);
                
                menuList.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuList;
    }
}
