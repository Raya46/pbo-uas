import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_restoran_final";
        String user = "root";
        String password = "";
        
        System.out.println("=== TEST KONEKSI DATABASE ===");
        System.out.println("URL: " + url);
        System.out.println("User: " + user);
        System.out.println("Password: " + (password.isEmpty() ? "(kosong)" : password));
        System.out.println("\nMencoba koneksi...\n");
        
        try {
            // Test 1: Load MySQL Driver
            System.out.println("✓ Loading MySQL Driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("  SUCCESS: MySQL Driver ditemukan!\n");
            
            // Test 2: Connect to MySQL Server (tanpa database)
            System.out.println("✓ Connecting to MySQL Server...");
            String serverUrl = "jdbc:mysql://localhost:3306/";
            Connection serverConn = DriverManager.getConnection(serverUrl, user, password);
            System.out.println("  SUCCESS: MySQL Server berjalan!\n");
            serverConn.close();
            
            // Test 3: Connect to specific database
            System.out.println("✓ Connecting to database 'db_restoran_final'...");
            Connection dbConn = DriverManager.getConnection(url, user, password);
            System.out.println("  SUCCESS: Database ditemukan dan koneksi berhasil!\n");
            dbConn.close();
            
            System.out.println("=============================");
            System.out.println("✅ SEMUA TEST BERHASIL!");
            System.out.println("Database siap digunakan.");
            System.out.println("=============================");
            
        } catch (ClassNotFoundException e) {
            System.err.println("\n❌ ERROR: MySQL Driver tidak ditemukan!");
            System.err.println("Solusi:");
            System.err.println("1. Download MySQL Connector JAR dari:");
            System.err.println("   https://dev.mysql.com/downloads/connector/j/");
            System.err.println("2. Tambahkan ke Project Libraries di IntelliJ");
            System.err.println("\nDetail Error: " + e.getMessage());
            
        } catch (SQLException e) {
            System.err.println("\n❌ ERROR: Koneksi Database Gagal!");
            System.err.println("\nKode Error: " + e.getErrorCode());
            System.err.println("Pesan: " + e.getMessage());
            System.err.println("\n=== KEMUNGKINAN PENYEBAB ===");
            
            if (e.getMessage().contains("Unknown database")) {
                System.err.println("→ Database 'db_restoran_final' belum dibuat");
                System.err.println("  Solusi: Buat database di phpMyAdmin");
                
            } else if (e.getMessage().contains("Communications link failure")) {
                System.err.println("→ MySQL Server tidak berjalan");
                System.err.println("  Solusi: Start MySQL di XAMPP Control Panel");
                
            } else if (e.getMessage().contains("Access denied")) {
                System.err.println("→ Username atau password salah");
                System.err.println("  Solusi: Cek kredensial MySQL Anda");
                
            } else {
                System.err.println("→ Error tidak dikenal");
                System.err.println("  Cek detail error di atas");
            }
        }
    }
}
