import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.math.BigDecimal;

public class ReportsFrame extends JFrame {

    private JSpinner dateSpinner;
    private JTable tableDaily;
    private JTable tableMonthly;

    public ReportsFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Laporan Penjualan");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // =============================
        // PANEL LAPORAN HARIAN
        // =============================
        JPanel dailyPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Pilih Tanggal: "));

        dateSpinner = new JSpinner(
                new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH)
        );
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        topPanel.add(dateSpinner);

        JButton btnLoadDaily = new JButton("Tampilkan");
        btnLoadDaily.addActionListener(e -> loadDailyReport());
        topPanel.add(btnLoadDaily);

        dailyPanel.add(topPanel, BorderLayout.NORTH);

        tableDaily = new JTable();
        dailyPanel.add(new JScrollPane(tableDaily), BorderLayout.CENTER);

        tabs.addTab("Laporan Harian", dailyPanel);

        // =============================
        // PANEL LAPORAN BULANAN
        // =============================
        JPanel monthlyPanel = new JPanel(new BorderLayout());

        JPanel topMonthly = new JPanel();
        JButton btnLoadMonthly = new JButton("Tampilkan Laporan Bulanan");
        btnLoadMonthly.addActionListener(e -> loadMonthlyReport());
        topMonthly.add(btnLoadMonthly);
        monthlyPanel.add(topMonthly, BorderLayout.NORTH);

        tableMonthly = new JTable();
        monthlyPanel.add(new JScrollPane(tableMonthly), BorderLayout.CENTER);

        tabs.addTab("Laporan Bulanan", monthlyPanel);

        add(tabs);
    }

    // =============================
    // DAILY REPORT
    // =============================
    private void loadDailyReport() {

        String query =
                "SELECT o.order_id AS order_id, o.customer_id AS customer_id, " +
                "SUM(d.subtotal) AS total_price, o.order_date AS order_date " +
                "FROM orders o " +
                "JOIN order_details d ON o.order_id = d.order_id " +
                "WHERE DATE(o.order_date) = ? " +
                "GROUP BY o.order_id, o.customer_id, o.order_date " +
                "ORDER BY o.order_date";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            Date utilDate = (Date) dateSpinner.getValue();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            ps.setDate(1, sqlDate);

            try (ResultSet rs = ps.executeQuery()) {
                tableDaily.setModel(buildTableModel(rs));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat laporan harian:\n" + ex.getMessage());
        }
    }

    // =============================
    // MONTHLY REPORT
    // =============================
    private void loadMonthlyReport() {

        String query =
                "SELECT DATE(o.order_date) AS tanggal, " +
                "SUM(d.subtotal) AS total_pendapatan " +
                "FROM orders o " +
                "JOIN order_details d ON o.order_id = d.order_id " +
                "WHERE DATE_FORMAT(o.order_date, '%Y-%m') = ? " +
                "GROUP BY DATE(o.order_date) " +
                "ORDER BY tanggal";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            Date selected = (Date) dateSpinner.getValue();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
            String ym = f.format(selected);
            ps.setString(1, ym);

            try (ResultSet rs = ps.executeQuery()) {
                tableMonthly.setModel(buildTableModel(rs));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat laporan bulanan:\n" + ex.getMessage());
        }
    }

    // =============================
    // RESULTSET → TABLEMODEL
    // =============================
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();

        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= colCount; i++) {
            // gunakan label alias, bukan nama fisik kolom DB
            columnNames.add(meta.getColumnLabel(i));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();

            for (int i = 1; i <= colCount; i++) {
                Object val = rs.getObject(i);

                if (val instanceof BigDecimal)
                    val = ((BigDecimal) val).toPlainString();

                if (val == null) val = "-";

                row.add(val);
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    // =============================
    // MAIN (untuk testing terpisah)
    // =============================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new ReportsFrame().setVisible(true));
    }
}
