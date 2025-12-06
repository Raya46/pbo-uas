import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Frame untuk melihat pesanan yang sedang aktif/pending
 * Kasir bisa lihat status pesanan dan update status
 */
public class CashierViewOrdersFrame extends JFrame {

    private final User currentUser;
    private final CashierMainFrame parentFrame;
    private JTable tableOrders;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboStatus;

    public CashierViewOrdersFrame(User user, CashierMainFrame parent) {
        this.currentUser = user;
        this.parentFrame = parent;
        initComponents();
        loadOrders();
    }

    private void initComponents() {
        setTitle("Pesanan Aktif - Kasir");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setPreferredSize(new Dimension(1000, 60));
        JLabel lblTitle = new JLabel("  📋 PESANAN AKTIF");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = { "Order ID", "Customer", "Phone", "Total", "Tanggal", "Status", "Payment" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableOrders = new JTable(tableModel);
        tableOrders.setRowHeight(30);
        tableOrders.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableOrders.setFont(new Font("Arial", Font.PLAIN, 12));
        tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableOrders);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomPanel.setBackground(new Color(236, 240, 241));

        JButton btnViewDetails = new JButton("Lihat Detail");
        btnViewDetails.setFont(new Font("Arial", Font.PLAIN, 14));
        btnViewDetails.setForeground(new Color(50, 50, 50));
        btnViewDetails.addActionListener(e -> viewOrderDetails());

        JButton btnUpdateStatus = new JButton("Update Status");
        btnUpdateStatus.setFont(new Font("Arial", Font.PLAIN, 14));
        btnUpdateStatus.setBackground(new Color(52, 152, 219));
        btnUpdateStatus.setForeground(new Color(50, 50, 50));
        btnUpdateStatus.setFocusPainted(false);
        btnUpdateStatus.addActionListener(e -> updateOrderStatus());

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRefresh.setForeground(new Color(50, 50, 50));
        btnRefresh.addActionListener(e -> loadOrders());

        JButton btnBack = new JButton("Kembali");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setForeground(new Color(50, 50, 50));
        btnBack.addActionListener(e -> goBack());

        bottomPanel.add(btnViewDetails);
        bottomPanel.add(btnUpdateStatus);
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnBack);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        tableModel.setRowCount(0);

        String sql = "SELECT o.id, c.full_name, c.phone, o.total_price, o.order_date, o.status, o.payment_method " +
                "FROM orders o " +
                "JOIN customers c ON o.customer_id = c.customer_id " +
                "WHERE o.status IN ('pending', 'cooking', 'served') " +
                "ORDER BY o.order_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        String.format("Rp %,.0f", rs.getDouble("total_price")),
                        rs.getTimestamp("order_date"),
                        rs.getString("status").toUpperCase(),
                        rs.getString("payment_method")
                };
                tableModel.addRow(row);
                count++;
            }

            if (count == 0) {
                JOptionPane.showMessageDialog(this,
                        "Tidak ada pesanan aktif saat ini.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading orders:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void viewOrderDetails() {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih order terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);

        // Get order details
        StringBuilder details = new StringBuilder();
        details.append("=== DETAIL PESANAN ===\n\n");
        details.append("Order ID: ").append(orderId).append("\n");
        details.append("Customer: ").append(tableModel.getValueAt(selectedRow, 1)).append("\n");
        details.append("Phone: ").append(tableModel.getValueAt(selectedRow, 2)).append("\n");
        details.append("Status: ").append(tableModel.getValueAt(selectedRow, 5)).append("\n\n");
        details.append("=== ITEMS ===\n");

        String sql = "SELECT m.name, od.quantity, od.price, (od.quantity * od.price) as subtotal " +
                "FROM order_details od " +
                "JOIN menu_items m ON od.menu_id = m.menu_id " +
                "WHERE od.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                details.append(String.format("• %s x%d @ Rp %,.0f = Rp %,.0f\n",
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDouble("subtotal")));
            }

            details.append("\n");
            details.append("TOTAL: ").append(tableModel.getValueAt(selectedRow, 3));

            JTextArea textArea = new JTextArea(details.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Detail Pesanan", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading details:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateOrderStatus() {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih order terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

        // Status options
        String[] statuses = { "pending", "cooking", "served", "paid", "cancelled" };
        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Pilih status baru untuk Order #" + orderId + ":",
                "Update Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                currentStatus.toLowerCase());

        if (newStatus == null)
            return; // User cancelled

        // Update database
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this,
                        "Status berhasil diupdate ke: " + newStatus.toUpperCase(),
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadOrders(); // Refresh table
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating status:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void goBack() {
        this.dispose();
        parentFrame.setVisible(true);
    }
}
