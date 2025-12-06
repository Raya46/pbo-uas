import java.sql.*;
import java.util.ArrayList;

public class MenuDAO {

    public ArrayList<MenuItem> getAllMenuItems() {
        ArrayList<MenuItem> menuList = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MenuItem m = new MenuItem(
                        rs.getInt("menu_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                menuList.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuList;
    }
}