import javax.swing.*;
import java.awt.*;

public class CustomerDashboard extends JFrame {

    private Customer customer;
    private Cart cart;

    // Constructor 1: Dipanggil jika Cart sudah ada (misal balik dari halaman menu)
    public CustomerDashboard(Customer customer, Cart cart) {
        this.customer = customer;
        this.cart = cart;
        initComponents();
    }

    // Constructor 2: Dipanggil pertama kali dari Login (Cart masih baru)
    public CustomerDashboard(Customer customer) {
        this.customer = customer;
        this.cart = new Cart();
        initComponents();
    }

    private void initComponents() {
        setTitle("Dashboard Customer - Restoran");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(600, 60));

        JLabel lblWelcome = new JLabel("Halo, " + customer.getFullName());
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.addActionListener(e -> logout());

        // Panel kecil untuk tombol logout agar rapi di kanan
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(btnLogout);

        header.add(lblWelcome, BorderLayout.WEST);
        header.add(logoutPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- MENU TOMBOL ---
        JPanel panelButtons = new JPanel(new GridLayout(3, 1, 10, 10));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));

        JButton btnViewMenu = new JButton("Lihat Menu");
        btnViewMenu.setFont(new Font("Arial", Font.PLAIN, 14));
        btnViewMenu.addActionListener(e -> {
            new CustomerViewMenuFrame(customer, cart).setVisible(true);
            dispose();
        });

        JButton btnCart = new JButton("Keranjang Pesanan");
        btnCart.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCart.addActionListener(e -> {
            new CustomerCartFrame(customer, cart).setVisible(true);
            dispose();
        });

        JButton btnOrderHistory = new JButton("Riwayat Pesanan");
        btnOrderHistory.setFont(new Font("Arial", Font.PLAIN, 14));
        btnOrderHistory.addActionListener(e -> {
            new CustomerOrderHistoryFrame(customer).setVisible(true);
            dispose();
        });

        panelButtons.add(btnViewMenu);
        panelButtons.add(btnCart);
        panelButtons.add(btnOrderHistory);

        add(panelButtons, BorderLayout.CENTER);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm().setVisible(true);
        }
    }
}