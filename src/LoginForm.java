import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login Sistem Restoran");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Login System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Memberi jarak atas bawah
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding kiri kanan

        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("")); // Spacer kosong di kiri
        btnLogin = new JButton("Login");
        formPanel.add(btnLogin);

        add(formPanel, BorderLayout.CENTER);

        btnLogin.addActionListener((ActionEvent e) -> {
            performLogin();
        });

        txtPassword.addActionListener((ActionEvent e) -> {
            performLogin();
        });
    }

    private void performLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        // Validasi input kosong
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Panggil UserDAO untuk cek ke database
        // Karena satu folder, tidak perlu import UserDAO
        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateLogin(username, password);

        if (user != null) {
            // Login Berhasil
            JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Datang, " + user.getFullName());

            this.dispose(); // Tutup jendela login

            if (user.getRole().equalsIgnoreCase("admin")) {
                // Buka Dashboard Admin
                new AdminMainFrame(user).setVisible(true);

            } else if (user.getRole().equalsIgnoreCase("kasir")) {
                JOptionPane.showMessageDialog(null, "Halaman Kasir akan segera hadir.");

            } else {
                JOptionPane.showMessageDialog(this, "Role user tidak dikenali!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            // Login Gagal
            JOptionPane.showMessageDialog(this, "Username atau Password Salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main Method agar file ini bisa di-Run langsung
    public static void main(String[] args) {
        // Mengatur tampilan agar mengikuti gaya sistem operasi (Windows/Mac/Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Gagal meload skin native: " + e.getMessage());
        }

        // Menjalankan Swing di Thread yang aman
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}