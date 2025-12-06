import javax.swing.*;
import java.awt.*;

/**
 * Dashboard utama untuk Kasir
 * Fitur: Buat Order, Lihat Order, Riwayat Transaksi
 */
public class CashierMainFrame extends JFrame {

    private final User currentUser;

    public CashierMainFrame(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Dashboard Kasir - Sistem Restoran");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== HEADER PANEL =====
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // ===== CONTENT PANEL =====
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(236, 240, 241));

        // Tombol Menu Utama
        JButton btnNewOrder = createMenuButton("Buat Pesanan Baru", new Color(52, 152, 219));
        JButton btnViewOrders = createMenuButton("Lihat Pesanan Aktif", new Color(46, 204, 113));
        JButton btnOrderHistory = createMenuButton("Riwayat Transaksi", new Color(155, 89, 182));
        JButton btnViewMenu = createMenuButton("Lihat Daftar Menu", new Color(241, 196, 15));

        // Event Listeners
        btnNewOrder.addActionListener(e -> openNewOrder());
        btnViewOrders.addActionListener(e -> openViewOrders());
        btnOrderHistory.addActionListener(e -> openOrderHistory());
        btnViewMenu.addActionListener(e -> openViewMenu());

        // Layout tombol
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(btnNewOrder, gbc);

        gbc.gridx = 1;
        contentPanel.add(btnViewOrders, gbc);

        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(btnOrderHistory, gbc);

        gbc.gridx = 1;
        contentPanel.add(btnViewMenu, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // ===== FOOTER PANEL =====
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(900, 40));
        JLabel lblFooter = new JLabel("© 2024 Sistem Manajemen Restoran - PBO UAS");
        lblFooter.setForeground(Color.WHITE);
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(900, 70));
        headerPanel.setLayout(new BorderLayout());

        // Left side - Title
        JLabel lblTitle = new JLabel("  💰 KASIR DASHBOARD");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        // Right side - User info & Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        rightPanel.setBackground(new Color(44, 62, 80));

        JLabel lblUser = new JLabel("👤 " + currentUser.getFullName() + " (Kasir)");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(new Color(50, 50, 50));
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> logout());

        rightPanel.add(lblUser);
        rightPanel.add(btnLogout);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(350, 80));
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(bgColor);

        // Use dark text for all menu buttons
        btn.setForeground(new Color(50, 50, 50));

        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        Color textColor = new Color(50, 50, 50);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
                btn.setForeground(textColor); // Keep text color consistent
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
                btn.setForeground(textColor);
            }
        });

        return btn;
    }

    private void openNewOrder() {
        this.setVisible(false);
        new CashierNewOrderFrame(currentUser, this).setVisible(true);
    }

    private void openViewOrders() {
        this.setVisible(false);
        new CashierViewOrdersFrame(currentUser, this).setVisible(true);
    }

    private void openOrderHistory() {
        JOptionPane.showMessageDialog(this,
                "Fitur Riwayat Transaksi\n\nMenampilkan semua transaksi yang sudah selesai.",
                "Order History",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void openViewMenu() {
        this.setVisible(false);
        new CashierViewMenuFrame(currentUser, this).setVisible(true);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginForm().setVisible(true);
        }
    }
}
