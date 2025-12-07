import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Dashboard Kasir - All-in-One
 * Fitur: Status Meja, Order List, Update Status, Pembayaran
 */
public class CashierMainFrame extends JFrame {

    private final User currentUser;
    private JTable tableTables;
    private DefaultTableModel tableTablesModel;
    private JTable tableOrders;
    private DefaultTableModel tableOrdersModel;
    private JLabel lblTotalOrders;
    private JLabel lblPendingOrders;

    public CashierMainFrame(User user) {
        this.currentUser = user;
        initComponents();
        loadTableStatus();
        loadOrders();
    }

    private void initComponents() {
        setTitle("Dashboard Kasir - " + currentUser.getFullName());
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(1400, 80));

        JLabel lblTitle = new JLabel("  DASHBOARD KASIR - OPERASIONAL");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel lblUser = new JLabel("Kasir: " + currentUser.getFullName() + "  ");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 16));

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblUser, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // ===== MAIN CONTENT =====
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Statistics Panel
        JPanel statsPanel = createStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.NORTH);

        // Split: Status Meja | Order List
        JPanel leftPanel = createTableStatusPanel();
        JPanel rightPanel = createOrderListPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.3);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomPanel.setBackground(new Color(236, 240, 241));

        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefresh.setBackground(Color.BLUE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setPreferredSize(new Dimension(160, 45));
        btnRefresh.addActionListener(e -> refreshAll());

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setFocusPainted(false);
        btnLogout.setPreferredSize(new Dimension(120, 45));
        btnLogout.addActionListener(e -> logout());

        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnLogout);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        lblTotalOrders = new JLabel("Total Orders Hari Ini: 0", SwingConstants.CENTER);
        lblTotalOrders.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalOrders.setOpaque(true);
        lblTotalOrders.setBackground(new Color(46, 204, 113));
        lblTotalOrders.setForeground(new Color(40, 40, 40));
        lblTotalOrders.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblPendingOrders = new JLabel("Pesanan Pending: 0", SwingConstants.CENTER);
        lblPendingOrders.setFont(new Font("Arial", Font.BOLD, 16));
        lblPendingOrders.setOpaque(true);
        lblPendingOrders.setBackground(new Color(243, 156, 18));
        lblPendingOrders.setForeground(new Color(40, 40, 40));
        lblPendingOrders.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(lblTotalOrders);
        panel.add(lblPendingOrders);

        return panel;
    }

    private JPanel createTableStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "STATUS MEJA",
                0, 0, new Font("Arial", Font.BOLD, 16)));

        String[] columns = { "No. Meja", "Status" };
        tableTablesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableTables = new JTable(tableTablesModel);
        tableTables.setRowHeight(35);
        tableTables.setFont(new Font("Arial", Font.PLAIN, 14));
        tableTables.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableTables.getTableHeader().setBackground(new Color(52, 152, 219));
        tableTables.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(tableTables);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createOrderListPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                "DAFTAR PESANAN MASUK",
                0, 0, new Font("Arial", Font.BOLD, 16)));

        String[] columns = { "Order ID", "Customer", "Meja", "Total", "Status", "Waktu" };
        tableOrdersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableOrders = new JTable(tableOrdersModel);
        tableOrders.setRowHeight(30);
        tableOrders.setFont(new Font("Arial", Font.PLAIN, 13));
        tableOrders.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableOrders.getTableHeader().setBackground(new Color(46, 204, 113));
        tableOrders.getTableHeader().setForeground(Color.black);

        JScrollPane scrollPane = new JScrollPane(tableOrders);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnViewDetail = new JButton("Lihat Detail");
        btnViewDetail.setFont(new Font("Arial", Font.PLAIN, 13));
        btnViewDetail.setBackground(new Color(52, 152, 219));
        btnViewDetail.setForeground(new Color(40, 40, 40));
        btnViewDetail.setFocusPainted(false);
        btnViewDetail.addActionListener(e -> viewOrderDetail());

        JButton btnUpdateStatus = new JButton("Update Status");
        btnUpdateStatus.setFont(new Font("Arial", Font.PLAIN, 13));
        btnUpdateStatus.setBackground(new Color(243, 156, 18));
        btnUpdateStatus.setForeground(new Color(40, 40, 40));
        btnUpdateStatus.setFocusPainted(false);
        btnUpdateStatus.addActionListener(e -> updateOrderStatus());

        JButton btnPayment = new JButton("Pembayaran");
        btnPayment.setFont(new Font("Arial", Font.BOLD, 13));
        btnPayment.setBackground(new Color(46, 204, 113));
        btnPayment.setForeground(new Color(40, 40, 40));
        btnPayment.setFocusPainted(false);
        btnPayment.addActionListener(e -> processPayment());

        actionPanel.add(btnViewDetail);
        actionPanel.add(btnUpdateStatus);
        actionPanel.add(btnPayment);

        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadTableStatus() {
        tableTablesModel.setRowCount(0);

        String sql = "SELECT table_number, status FROM restaurant_tables ORDER BY table_number";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");
                String statusDisplay = status.equals("available") ? "Available" : "Occupied";

                Object[] row = {
                        "Meja " + rs.getString("table_number"),
                        statusDisplay
                };
                tableTablesModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading table status: " + e.getMessage());
        }
    }

    private void loadOrders() {
        tableOrdersModel.setRowCount(0);

        String sql = "SELECT o.order_id, c.customer_name, rt.table_number, o.total_amount, " +
                "o.status, o.order_date " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.customer_id " +
                "LEFT JOIN restaurant_tables rt ON o.table_id = rt.table_id " +
                "WHERE DATE(o.order_date) = CURDATE() " +
                "ORDER BY o.order_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            int totalOrders = 0;
            int pendingOrders = 0;
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            while (rs.next()) {
                String tableName = rs.getString("table_number");
                if (tableName == null)
                    tableName = "Take Away";

                String status = rs.getString("status");
                String statusDisplay = getStatusDisplay(status);

                Object[] row = {
                        "#" + rs.getInt("order_id"),
                        rs.getString("customer_name"),
                        tableName,
                        String.format("Rp %,.0f", rs.getDouble("total_amount")),
                        statusDisplay,
                        timeFormat.format(rs.getTimestamp("order_date"))
                };
                tableOrdersModel.addRow(row);

                totalOrders++;
                if (status.equals("pending"))
                    pendingOrders++;
            }

            lblTotalOrders.setText("Total Orders Hari Ini: " + totalOrders);
            lblPendingOrders.setText("Pesanan Pending: " + pendingOrders);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading orders: " + e.getMessage());
        }
    }

    private String getStatusDisplay(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return "Pending";
            case "cooking":
                return "Cooking";
            case "ready":
                return "Ready";
            case "served":
                return "Served";
            case "paid":
                return "Paid";
            case "cancelled":
                return "Cancelled";
            default:
                return status;
        }
    }

    private void viewOrderDetail() {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih order terlebih dahulu!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderIdStr = (String) tableOrdersModel.getValueAt(selectedRow, 0);
        int orderId = Integer.parseInt(orderIdStr.replace("#", ""));

        StringBuilder detail = new StringBuilder();
        detail.append("═══════════════════════════════════\n");
        detail.append("         DETAIL PESANAN\n");
        detail.append("═══════════════════════════════════\n\n");

        String sql = "SELECT o.*, c.customer_name, c.phone_number, rt.table_number " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.customer_id " +
                "LEFT JOIN restaurant_tables rt ON o.table_id = rt.table_id " +
                "WHERE o.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                detail.append("Order ID      : #").append(orderId).append("\n");
                detail.append("Customer      : ").append(rs.getString("customer_name")).append("\n");
                detail.append("No. HP        : ").append(rs.getString("phone_number")).append("\n");

                String tableNum = rs.getString("table_number");
                detail.append("Meja          : ").append(tableNum != null ? tableNum : "Take Away").append("\n");

                detail.append("Status        : ").append(getStatusDisplay(rs.getString("status"))).append("\n");
                detail.append("Metode Bayar  : ").append(rs.getString("payment_method").toUpperCase()).append("\n");
                detail.append("Waktu         : ").append(rs.getTimestamp("order_date")).append("\n\n");
                detail.append("───────────────────────────────────\n");
                detail.append("ITEMS:\n");
                detail.append("───────────────────────────────────\n\n");
            }

            String sqlItems = "SELECT od.*, m.name " +
                    "FROM order_details od " +
                    "JOIN menu_items m ON od.menu_id = m.menu_id " +
                    "WHERE od.order_id = ?";

            PreparedStatement psItems = conn.prepareStatement(sqlItems);
            psItems.setInt(1, orderId);
            ResultSet rsItems = psItems.executeQuery();

            double total = 0;
            while (rsItems.next()) {
                String itemName = rsItems.getString("name");
                int qty = rsItems.getInt("quantity");
                double subtotal = rsItems.getDouble("subtotal");
                String note = rsItems.getString("note");

                detail.append(String.format("%-25s x%d\n", itemName, qty));
                detail.append(String.format("  Rp %,15.0f\n", subtotal));
                if (note != null && !note.isEmpty()) {
                    detail.append("  Note: ").append(note).append("\n");
                }
                detail.append("\n");
                total += subtotal;
            }

            detail.append("───────────────────────────────────\n");
            detail.append(String.format("TOTAL:        Rp %,.0f\n", total));
            detail.append("═══════════════════════════════════\n");

            JTextArea textArea = new JTextArea(detail.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(450, 500));

            JOptionPane.showMessageDialog(this, scrollPane, "Detail Order #" + orderId,
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateOrderStatus() {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih order terlebih dahulu!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderIdStr = (String) tableOrdersModel.getValueAt(selectedRow, 0);
        int orderId = Integer.parseInt(orderIdStr.replace("#", ""));
        String currentStatus = (String) tableOrdersModel.getValueAt(selectedRow, 4);

        String[] statuses = { "Pending", "Cooking", "Ready", "Served", "Paid", "Cancelled" };
        String[] statusValues = { "pending", "cooking", "ready", "served", "paid", "cancelled" };

        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Pilih status baru untuk Order #" + orderId + ":",
                "Update Status Pesanan",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statuses,
                currentStatus);

        if (selected != null) {
            int index = java.util.Arrays.asList(statuses).indexOf(selected);
            String newStatus = statusValues[index];

            String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, newStatus);
                ps.setInt(2, orderId);

                int updated = ps.executeUpdate();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Status order berhasil diupdate ke: " + selected,
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshAll();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void processPayment() {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih order terlebih dahulu!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderIdStr = (String) tableOrdersModel.getValueAt(selectedRow, 0);
        int orderId = Integer.parseInt(orderIdStr.replace("#", ""));
        String currentStatus = (String) tableOrdersModel.getValueAt(selectedRow, 4);

        if (currentStatus.contains("Paid")) {
            JOptionPane.showMessageDialog(this, "Order ini sudah dibayar!", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String receipt = generateReceipt(orderId);
        if (receipt == null)
            return;

        JTextArea textArea = new JTextArea(receipt);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 500));

        int confirm = JOptionPane.showConfirmDialog(this, scrollPane,
                "STRUK PEMBAYARAN - Konfirmasi",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (confirm == JOptionPane.OK_OPTION) {
            String sql = "UPDATE orders SET status = 'paid' WHERE order_id = ?";
            String sqlTable = "UPDATE restaurant_tables rt " +
                    "JOIN orders o ON rt.table_id = o.table_id " +
                    "SET rt.status = 'available' " +
                    "WHERE o.order_id = ?";

            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, orderId);
                ps.executeUpdate();

                PreparedStatement psTable = conn.prepareStatement(sqlTable);
                psTable.setInt(1, orderId);
                psTable.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Pembayaran berhasil!\nStruk telah dicetak (simulasi).\nMeja telah dirilis.",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);

                refreshAll();

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private String generateReceipt(int orderId) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("═══════════════════════════════════\n");
        receipt.append("       RESTORAN NUSANTARA\n");
        receipt.append("     Jl. Merdeka No. 123\n");
        receipt.append("      Telp: (021) 1234567\n");
        receipt.append("═══════════════════════════════════\n\n");

        String sql = "SELECT o.*, c.customer_name, c.phone_number, rt.table_number " +
                "FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.customer_id " +
                "LEFT JOIN restaurant_tables rt ON o.table_id = rt.table_id " +
                "WHERE o.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                receipt.append("No. Order : #").append(orderId).append("\n");
                receipt.append("Tanggal   : ").append(sdf.format(rs.getTimestamp("order_date"))).append("\n");
                receipt.append("Kasir     : ").append(currentUser.getFullName()).append("\n");
                receipt.append("Customer  : ").append(rs.getString("customer_name")).append("\n");

                String tableNum = rs.getString("table_number");
                if (tableNum != null) {
                    receipt.append("Meja      : ").append(tableNum).append("\n");
                }

                receipt.append("\n───────────────────────────────────\n\n");
            }

            String sqlItems = "SELECT od.*, m.name " +
                    "FROM order_details od " +
                    "JOIN menu_items m ON od.menu_id = m.menu_id " +
                    "WHERE od.order_id = ?";

            PreparedStatement psItems = conn.prepareStatement(sqlItems);
            psItems.setInt(1, orderId);
            ResultSet rsItems = psItems.executeQuery();

            double total = 0;
            while (rsItems.next()) {
                String name = rsItems.getString("name");
                int qty = rsItems.getInt("quantity");
                double subtotal = rsItems.getDouble("subtotal");
                double price = subtotal / qty;

                receipt.append(String.format("%-20s x%d\n", name, qty));
                receipt.append(String.format("  @Rp%,.0f = Rp%,.0f\n", price, subtotal));
                total += subtotal;
            }

            receipt.append("\n───────────────────────────────────\n");
            receipt.append(String.format("TOTAL:          Rp %,.0f\n", total));
            receipt.append("═══════════════════════════════════\n\n");
            receipt.append("     Terima Kasih Atas\n");
            receipt.append("       Kunjungan Anda!\n\n");
            receipt.append("═══════════════════════════════════\n");

            return receipt.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating receipt: " + e.getMessage());
            return null;
        }
    }

    private void refreshAll() {
        loadTableStatus();
        loadOrders();
        JOptionPane.showMessageDialog(this, "Data berhasil di-refresh!", "Info",
                JOptionPane.INFORMATION_MESSAGE);
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
