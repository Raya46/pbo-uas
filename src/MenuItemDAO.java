import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {

    //Ambil semua menu, beserta nama kategorinya (LEFT JOIN)
    public List<MenuItem> findAll() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT m.menu_id, m.category_id, c.category_name, m.name, m.description, m.price, m.stock, m.image_path, m.is_available " +
                     "FROM menu_items m LEFT JOIN categories c ON m.category_id = c.category_id " +
                "ORDER BY m.menu_id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MenuItem mi = mapRowToMenuItem(rs);
                items.add(mi);
            }

        } catch (SQLException e) {
            System.err.println("MenuItemDAO.findAll Error: " + e.getMessage());
        }

        return items;
    }

    //Cari menu berdasarkan ID
    public MenuItem findById(int menuId) {
        String sql = "SELECT m.menu_id, m.category_id, c.category_name, m.name, m.description, m.price, m.stock, m.image_path, m.is_available " +
                     "FROM menu_items m LEFT JOIN categories c ON m.category_id = c.category_id WHERE m.menu_id = ?";
        MenuItem mi = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, menuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mi = mapRowToMenuItem(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("MenuItemDAO.findById Error: " + e.getMessage());
        }

        return mi;
    }

    //Cari menu berdasarkan kategori
    public List<MenuItem> findByCategory(int categoryId) {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT m.menu_id, m.category_id, c.category_name, m.name, m.description, m.price, m.stock, m.image_path, m.is_available " +
                     "FROM menu_items m LEFT JOIN categories c ON m.category_id = c.category_id WHERE m.category_id = ? ORDER BY m.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapRowToMenuItem(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("MenuItemDAO.findByCategory Error: " + e.getMessage());
        }

        return items;
    }

    //Insert menu baru, mengembalikan generated menu_id atau -1 jika gagal
    public int insert(MenuItem item) {
        String sql = "INSERT INTO menu_items (category_id, name, description, price, stock, image_path, is_available) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (item.getCategory() != null) {
                ps.setInt(1, item.getCategory().getCategoryId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setBigDecimal(4, item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO);
            ps.setInt(5, item.getStock());
            ps.setString(6, item.getImagePath());
            ps.setBoolean(7, item.isAvailable());

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    generatedId = keys.getInt(1);
                    item.setMenuId(generatedId);
                }
            }

        } catch (SQLException e) {
            System.err.println("MenuItemDAO.insert Error: " + e.getMessage());
        }

        return generatedId;
    }

    //Update menu existing

    public boolean update(MenuItem item) {
        String sql = "UPDATE menu_items SET category_id = ?, name = ?, description = ?, price = ?, stock = ?, image_path = ?, is_available = ? WHERE menu_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (item.getCategory() != null) {
                ps.setInt(1, item.getCategory().getCategoryId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setBigDecimal(4, item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO);
            ps.setInt(5, item.getStock());
            ps.setString(6, item.getImagePath());
            ps.setBoolean(7, item.isAvailable());
            ps.setInt(8, item.getMenuId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("MenuItemDAO.update Error: " + e.getMessage());
            return false;
        }
    }

    //Hapus menu berdasarkan ID

    public boolean delete(int menuId) {
        String sql = "DELETE FROM menu_items WHERE menu_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, menuId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("MenuItemDAO.delete Error: " + e.getMessage());
            return false;
        }
    }

    //Update stock (utility)
    public boolean updateStock(int menuId, int newStock) {
        String sql = "UPDATE menu_items SET stock = ? WHERE menu_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newStock);
            ps.setInt(2, menuId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("MenuItemDAO.updateStock Error: " + e.getMessage());
            return false;
        }
    }

    //Set availability (utility)
    public boolean setAvailability(int menuId, boolean available) {
        String sql = "UPDATE menu_items SET is_available = ? WHERE menu_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, menuId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("MenuItemDAO.setAvailability Error: " + e.getMessage());
            return false;
        }
    }

    // Helper: mapping ResultSet -> MenuItem
    private MenuItem mapRowToMenuItem(ResultSet rs) throws SQLException {
        MenuItem mi = new MenuItem();
        mi.setMenuId(rs.getInt("menu_id"));

        int catId = rs.getInt("category_id");
        if (rs.wasNull()) {
            mi.setCategory(null);
        } else {
            Category cat = new Category();
            cat.setCategoryId(catId);
            cat.setCategoryName(rs.getString("category_name"));
            mi.setCategory(cat);
        }

        mi.setName(rs.getString("name"));
        mi.setDescription(rs.getString("description"));
        mi.setPrice(rs.getBigDecimal("price"));
        mi.setStock(rs.getInt("stock"));
        mi.setImagePath(rs.getString("image_path"));
        mi.setAvailable(rs.getBoolean("is_available"));

        return mi;
    }
}
 