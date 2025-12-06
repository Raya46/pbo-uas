import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Frame untuk membuat pesanan baru (POS System)
 * Kasir bisa pilih menu, tambah ke cart, dan checkout
 */
public class CashierNewOrderFrame extends JFrame {

    private final User currentUser;
    private final CashierMainFrame parentFrame;

    // Components
    private JTable tableMenu;
    private DefaultTableModel menuTableModel;
    private JTable tableCart;
    private DefaultTableModel cartTableModel;

    private JTextField txtCustomerName;
    private JTextField txtCustomerPhone;
    private JSpinner spinnerQuantity;
    private JLabel lblTotal;

    private ArrayList<MenuItem> menuList;
    private ArrayList<CartItem> cartItems;
    private double totalPrice = 0.0;

    public CashierNewOrderFrame(User user, CashierMainFrame parent) {
        this.currentUser = user;
        this.parentFrame = parent;
        this.cartItems = new ArrayList<>();
        initComponents();
        loadMenuData();
    }

    private void initComponents() {
        setTitle("Buat Pesanan Baru - Kasir");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        JLabel lblTitle = new JLabel("  📝 BUAT PESANAN BARU");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // ===== MAIN PANEL (Split: Menu List | Cart) =====
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // LEFT: Menu List
        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel);

        // RIGHT: Cart & Customer Info
        JPanel cartPanel = createCartPanel();
        mainPanel.add(cartPanel);

        add(mainPanel, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Menu"));

        // Table Menu
        String[] columns = { "ID", "Nama", "Kategori", "Harga", "Stok" };
        menuTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableMenu = new JTable(menuTableModel);
        tableMenu.setRowHeight(25);
        tableMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableMenu);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add to Cart Panel
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        addPanel.add(new JLabel("Jumlah:"));

        spinnerQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinnerQuantity.setPreferredSize(new Dimension(80, 30));
        addPanel.add(spinnerQuantity);

        JButton btnAddToCart = new JButton("Tambah ke Keranjang");
        btnAddToCart.setBackground(new Color(46, 204, 113));
        btnAddToCart.setForeground(new Color(50, 50, 50));
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.addActionListener(e -> addToCart());
        addPanel.add(btnAddToCart);

        panel.add(addPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Keranjang Belanja"));

        // Customer Info
        JPanel customerPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        customerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        customerPanel.add(new JLabel("Nama Customer:"));
        txtCustomerName = new JTextField();
        customerPanel.add(txtCustomerName);

        customerPanel.add(new JLabel("No. HP:"));
        txtCustomerPhone = new JTextField();
        customerPanel.add(txtCustomerPhone);

        panel.add(customerPanel, BorderLayout.NORTH);

        // Cart Table
        String[] columns = { "Menu", "Harga", "Qty", "Subtotal" };
        cartTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCart = new JTable(cartTableModel);
        tableCart.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableCart);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Total & Actions
        JPanel totalPanel = new JPanel(new BorderLayout());

        lblTotal = new JLabel("TOTAL: Rp 0", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(new Color(231, 76, 60));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        totalPanel.add(lblTotal, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        JButton btnRemove = new JButton("Hapus Item");
        btnRemove.setForeground(new Color(50, 50, 50));
        btnRemove.addActionListener(e -> removeFromCart());
        actionPanel.add(btnRemove);

        JButton btnClear = new JButton("Kosongkan");
        btnClear.setForeground(new Color(50, 50, 50));
        btnClear.addActionListener(e -> clearCart());
        actionPanel.add(btnClear);

        totalPanel.add(actionPanel, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(new Color(236, 240, 241));

        JButton btnCheckout = new JButton("CHECKOUT & BAYAR");
        btnCheckout.setPreferredSize(new Dimension(200, 45));
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 16));
        btnCheckout.setBackground(new Color(46, 204, 113));
        btnCheckout.setForeground(new Color(50, 50, 50));
        btnCheckout.setFocusPainted(false);
        btnCheckout.addActionListener(e -> checkout());

        JButton btnBack = new JButton("Kembali");
        btnBack.setPreferredSize(new Dimension(150, 45));
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setForeground(new Color(50, 50, 50));
        btnBack.addActionListener(e -> goBack());

        panel.add(btnCheckout);
        panel.add(btnBack);

        return panel;
    }

    private void loadMenuData() {
        menuTableModel.setRowCount(0);

        MenuDAO menuDAO = new MenuDAO();
        menuList = menuDAO.getAllMenuItems();

        for (MenuItem item : menuList) {
            if (item.getStock() > 0) { // Only show available items
                Object[] row = {
                        item.getMenuId(),
                        item.getName(),
                        item.getCategory(),
                        String.format("Rp %,.0f", item.getPrice()),
                        item.getStock()
                };
                menuTableModel.addRow(row);
            }
        }
    }

    private void addToCart() {
        int selectedRow = tableMenu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih menu terlebih dahulu!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int menuId = (int) menuTableModel.getValueAt(selectedRow, 0);
        int quantity = (int) spinnerQuantity.getValue();

        // Find menu item
        MenuItem selectedMenu = null;
        for (MenuItem item : menuList) {
            if (item.getMenuId() == menuId) {
                selectedMenu = item;
                break;
            }
        }

        if (selectedMenu == null)
            return;

        // Check stock
        if (quantity > selectedMenu.getStock()) {
            JOptionPane.showMessageDialog(this,
                    "Stok tidak cukup!\nStok tersedia: " + selectedMenu.getStock(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if item already in cart
        boolean found = false;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getMenuItem().getMenuId() == menuId) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            cartItems.add(new CartItem(selectedMenu, quantity));
        }

        updateCartTable();
        spinnerQuantity.setValue(1); // Reset quantity
    }

    private void removeFromCart() {
        int selectedRow = tableCart.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item yang akan dihapus!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        cartItems.remove(selectedRow);
        updateCartTable();
    }

    private void clearCart() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin mengosongkan keranjang?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cartItems.clear();
            updateCartTable();
        }
    }

    private void updateCartTable() {
        cartTableModel.setRowCount(0);
        totalPrice = 0.0;

        for (CartItem item : cartItems) {
            double subtotal = item.getTotalPrice();
            totalPrice += subtotal;

            Object[] row = {
                    item.getMenuItem().getName(),
                    String.format("Rp %,.0f", item.getMenuItem().getPrice()),
                    item.getQuantity(),
                    String.format("Rp %,.0f", subtotal)
            };
            cartTableModel.addRow(row);
        }

        lblTotal.setText(String.format("TOTAL: Rp %,.0f", totalPrice));
    }

    private void checkout() {
        // Validation
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String customerName = txtCustomerName.getText().trim();
        String customerPhone = txtCustomerPhone.getText().trim();

        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama customer harus diisi!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirm checkout
        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Total Pembayaran: Rp %,.0f\n\nProses pembayaran?", totalPrice),
                "Konfirmasi Checkout",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION)
            return;

        try {
            // 1. Create or get customer
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.login(customerPhone, "default"); // Try to find existing

            int customerId;
            if (customer == null) {
                // Create new customer (simplified - in real app, should have proper
                // registration)
                customerId = createQuickCustomer(customerName, customerPhone);
            } else {
                customerId = customer.getCustomerId();
            }

            // 2. Create order
            OrderDAO orderDAO = new OrderDAO();
            Order order = new Order(customerId, totalPrice, new Date());
            int orderId = orderDAO.createOrder(order);

            if (orderId == -1) {
                throw new SQLException("Failed to create order");
            }

            // 3. Add order details
            for (CartItem item : cartItems) {
                OrderDetail detail = new OrderDetail(
                        orderId,
                        item.getMenuItem().getMenuId(),
                        item.getQuantity(),
                        item.getMenuItem().getPrice());
                orderDAO.addOrderDetail(detail);
            }

            // 4. Success
            JOptionPane.showMessageDialog(this,
                    "✅ Pembayaran Berhasil!\n\n" +
                            "Order ID: " + orderId + "\n" +
                            "Total: Rp " + String.format("%,.0f", totalPrice) + "\n\n" +
                            "Terima kasih!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear and go back
            cartItems.clear();
            updateCartTable();
            txtCustomerName.setText("");
            txtCustomerPhone.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saat menyimpan order:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private int createQuickCustomer(String name, String phone) throws SQLException {
        // This is a simplified version - in production, use proper CustomerDAO method
        java.sql.Connection conn = DatabaseConnection.getConnection();
        String sql = "INSERT INTO customers (username, password, full_name, phone) VALUES (?, ?, ?, ?)";
        java.sql.PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, phone); // Use phone as username
        ps.setString(2, "default"); // Default password
        ps.setString(3, name);
        ps.setString(4, phone);
        ps.executeUpdate();

        java.sql.ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int id = rs.getInt(1);
            rs.close();
            ps.close();
            conn.close();
            return id;
        }

        throw new SQLException("Failed to create customer");
    }

    private void goBack() {
        if (!cartItems.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Keranjang belum di-checkout. Yakin ingin kembali?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION)
                return;
        }

        this.dispose();
        parentFrame.setVisible(true);
    }
}
