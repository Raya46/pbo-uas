import javax.swing.*;
import java.awt.*;

public class AdminMainFrame extends JFrame {

    final private User currentUser;

    public AdminMainFrame(User user) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Dashboard Admin - Sistem Restoran");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center screen

        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80)); // Warna gelap
        headerPanel.setPreferredSize(new Dimension(800, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 15));

        JLabel lblUser = new JLabel("Selamat Datang, " + currentUser.getFullName() + " (Admin)");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> logout());

        headerPanel.add(lblUser);
        headerPanel.add(btnLogout);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout()); // Agar tombol di tengah

        JButton btnManageMenu = createMenuButton("Manajemen Menu (Makanan/Minuman)");
        JButton btnManageUsers = createMenuButton("Manajemen User");
        JButton btnReports = createMenuButton("Laporan Penjualan");

        btnManageMenu.addActionListener(e -> {
            System.out.println("DEBUG: btnManageMenu clicked");
            try {
                MenuManagementFrame menuFrame = new MenuManagementFrame(AdminMainFrame.this);
                menuFrame.setVisible(true);
                AdminMainFrame.this.setVisible(false);

                menuFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent ev) {
                        AdminMainFrame.this.setVisible(true);
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(AdminMainFrame.this,
                        "Gagal membuka Menu Management:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnManageUsers.addActionListener(e -> {
            this.setVisible(false); // sembunyikan AdminMainFrame
            UserManagementFrame umf = new UserManagementFrame(this);
            umf.setVisible(true);
        });

        btnReports.addActionListener(e -> {
            try {
                ReportsFrame r = new ReportsFrame();
                r.setVisible(true);
                AdminMainFrame.this.setVisible(false);

                r.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent ev) {
                        AdminMainFrame.this.setVisible(true);
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(AdminMainFrame.this,
                        "Gagal membuka Halaman Laporan:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Menambahkan tombol ke panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Jarak antar tombol
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(btnManageMenu, gbc);

        gbc.gridy = 1;
        contentPanel.add(btnManageUsers, gbc);

        gbc.gridy = 2;
        contentPanel.add(btnReports, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(300, 50));
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        return btn;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginForm().setVisible(true);
        }
    }
}
