import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Frame untuk melihat daftar menu
 * Kasir bisa lihat menu apa saja yang tersedia
 */
public class CashierViewMenuFrame extends JFrame {

    private final User currentUser;
    private final CashierMainFrame parentFrame;
    private JTable tableMenu;
    private DefaultTableModel tableModel;

    public CashierViewMenuFrame(User user, CashierMainFrame parent) {
        this.currentUser = user;
        this.parentFrame = parent;
        initComponents();
        loadMenuData();
    }

    private void initComponents() {
        setTitle("Daftar Menu - Kasir");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(900, 60));

        JLabel lblTitle = new JLabel("  DAFTAR MENU RESTORAN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(lblTitle);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Nama Menu", "Kategori", "Harga", "Stok", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };

        tableMenu = new JTable(tableModel);
        tableMenu.setRowHeight(30);
        tableMenu.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableMenu.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(tableMenu);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRefresh.setForeground(new Color(50, 50, 50));
        btnRefresh.addActionListener(e -> loadMenuData());

        JButton btnBack = new JButton("Kembali");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setForeground(new Color(50, 50, 50));
        btnBack.addActionListener(e -> goBack());

        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnBack);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMenuData() {
        // Clear existing data
        tableModel.setRowCount(0);

        MenuDAO menuDAO = new MenuDAO();
        ArrayList<MenuItem> menuList = menuDAO.getAllMenuItems();

        for (MenuItem item : menuList) {
            String status = item.getStock() > 0 ? "Tersedia" : "Habis";
            Object[] row = {
                    item.getMenuId(),
                    item.getName(),
                    item.getCategory(),
                    String.format("Rp %,.0f", item.getPrice()),
                    item.getStock(),
                    status
            };
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this,
                "Data menu berhasil dimuat!\nTotal: " + menuList.size() + " item",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void goBack() {
        this.dispose();
        parentFrame.setVisible(true);
    }
}
