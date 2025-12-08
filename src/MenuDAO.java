import java.sql.*;

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
}
