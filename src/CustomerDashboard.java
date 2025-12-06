import javax.swing.*;
import java.awt.*;

public class CustomerDashboard extends JFrame {

    private Customer customer;
    private Cart cart;

    public CustomerDashboard(Customer customer, Cart cart) {
        this.customer = customer;
        this.cart = cart;
        initComponents();
    }

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

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(44, 62, 80));
        header.setPreferredSize(new Dimension(600, 60));

        JLabel lblWelcome = new JLabel("Selamat Datang, " + customer.getFullName());
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> logout());

        header.add(lblWelcome, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel panelButtons = new JPanel(new GridLayout(3,1,10,10));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(50,150,50,150));

        JButton btnViewMenu = new JButton("Lihat Menu");
        btnViewMenu.addActionListener(e -> {
            new CustomerViewMenuFrame(customer, cart).setVisible(true);
            dispose();
        });

        JButton btnCart = new JButton("Keranjang Pesanan");
        btnCart.addActionListener(e -> {
            new CustomerCartFrame(customer, cart).setVisible(true);
            dispose();
        });

        JButton btnOrderHistory = new JButton("Riwayat Pesanan");
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
            new CustomerLoginForm().setVisible(true);
        }
    }
}