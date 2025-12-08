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
        setLocationRelativeTo(null); // Agar muncul tepat di tengah layar
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("Login System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form Input
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("")); // Spacer kosong
        btnLogin = new JButton("Login");
        formPanel.add(btnLogin);

        add(formPanel, BorderLayout.CENTER);

        // Event Actions
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

        // 1. Validasi input kosong
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Cek ke database via UserDAO untuk semua role
        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateLogin(username, password);

        if (user != null) {
            // Login Berhasil
            JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Datang, " + user.getFullName());

            this.dispose(); // Tutup jendela login

            // 3. Arahkan sesuai Role
            if (user.getRole().equalsIgnoreCase("admin")) {
                new AdminMainFrame(user).setVisible(true);

            } else if (user.getRole().equalsIgnoreCase("kasir")) {
                new CashierMainFrame(user).setVisible(true);

            } else if (user.getRole().equalsIgnoreCase("customer")) {
                // Convert User ke Customer untuk compatibility dengan frame customer
                Customer customer = new Customer(user.getUserId(), user.getUsername(), user.getPassword(), user.getFullName());
                new CustomerDashboard(customer).setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "Role user tidak dikenali: " + user.getRole(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            // Login Gagal
            JOptionPane.showMessageDialog(this, "Username atau Password Salah!", "Login Gagal",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Gagal meload skin native: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}