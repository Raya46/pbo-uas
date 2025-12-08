import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class UserManagementFrame extends JFrame {

    private JTextField txtSearch;
    private JTable tableUser;
    private DefaultTableModel tableModel;

    private JTextField txtFullName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private AdminMainFrame parent;

    private JButton btnReset, btnAdd, btnSave, btnDelete, btnBack;

    private final UserDAO userDao = new UserDAO();
    private Integer currentUserId = null;

    // MAIN CONSTRUCTOR (dipakai dari AdminMainFrame)
    public UserManagementFrame(AdminMainFrame parent) {
        super("Manajemen User");
        this.parent = parent;

        initComponents();
        loadUserData();
        initActions();

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // CONSTRUCTOR untuk testing manual
    public UserManagementFrame() {
        this(null);
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        // ================= NORTH (SEARCH) =================
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtSearch = new JTextField(25);
        northPanel.add(new JLabel("🔍 Cari:"));
        northPanel.add(txtSearch);
        add(northPanel, BorderLayout.NORTH);

        // ================= CENTER (TABLE) =================
        String[] cols = {"ID", "Nama", "Username", "Role"};

        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tableUser = new JTable(tableModel);
        tableUser.setRowHeight(28);
        tableUser.setFillsViewportHeight(true);

        // SCROLLPANE BACKGROUND FIX
        JScrollPane scroll = new JScrollPane(tableUser);
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // HEADER STYLE FIX (agar konsisten)
        JTableHeader header = tableUser.getTableHeader();
        header.setReorderingAllowed(false);

        Color headerBg = new Color(0, 51, 102);
        Color headerFg = Color.WHITE;

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setBackground(headerBg);
        headerRenderer.setForeground(headerFg);
        headerRenderer.setOpaque(true);

        for (int i = 0; i < tableUser.getColumnCount(); i++) {
            tableUser.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }


        // ROW STYLE (striped)
        DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer() {
            private final Color even = Color.WHITE;
            private final Color odd = new Color(240, 240, 240);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object val,
                                                          boolean isSelected, boolean hasFocus,
                                                          int row, int col) {

                Component c = super.getTableCellRendererComponent(table, val, isSelected, hasFocus, row, col);

                if (isSelected) {
                    c.setBackground(new Color(0, 120, 215));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground((row % 2 == 0) ? even : odd);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };

        for (int i = 0; i < tableUser.getColumnCount(); i++) {
            tableUser.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }


        // ================= SOUTH (FORM) =================
        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(BorderFactory.createTitledBorder("Form User"));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // LABEL STYLE
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lblName = new JLabel("Nama Lengkap:");
        lblName.setFont(labelFont);
        form.add(lblName, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        txtFullName = new JTextField();
        txtFullName.setPreferredSize(new Dimension(300, 26));
        form.add(txtFullName, gbc);

        // Reset width
        gbc.gridwidth = 1; gbc.weightx = 0;

        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(labelFont);
        form.add(lblUsername, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(250, 26));
        form.add(txtUsername, gbc);

        gbc.gridwidth = 1; gbc.weightx = 0;

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(labelFont);
        form.add(lblPass, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(250, 26));
        form.add(txtPassword, gbc);

        gbc.gridwidth = 1; gbc.weightx = 0;

        // Role
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblRole = new JLabel("Role:");
        lblRole.setFont(labelFont);
        form.add(lblRole, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1;
        cmbRole = new JComboBox<>(new String[]{"Admin", "Kasir", "Dapur"});
        cmbRole.setPreferredSize(new Dimension(150, 26));
        form.add(cmbRole, gbc);

        south.add(form, BorderLayout.CENTER);

        // BUTTONS
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnBack = new JButton("⬅ Kembali");
        btnReset = new JButton("🔄 Reset");
        btnAdd = new JButton("➕ Tambah");
        btnSave = new JButton("✏️ Simpan");
        btnDelete = new JButton("🗑 Hapus");

        btnPanel.add(btnBack);
        btnPanel.add(btnReset);
        btnPanel.add(btnAdd);
        btnPanel.add(btnSave);
        btnPanel.add(btnDelete);

        south.add(btnPanel, BorderLayout.SOUTH);

        add(south, BorderLayout.SOUTH);
    }

    private void initActions() {

        // Search live
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });

        // Load form when table row clicked
        tableUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tableUser.getSelectedRow();
                if (r >= 0) {
                    int id = (int) tableModel.getValueAt(r, 0);
                    loadToForm(id);
                }
            }
        });

        btnReset.addActionListener(e -> resetForm());
        btnAdd.addActionListener(e -> addUser());

        btnSave.addActionListener(e -> {
            if (currentUserId == null) {
                JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu!");
                return;
            }
            updateUser();
        });

        btnDelete.addActionListener(e -> deleteUser());

        btnBack.addActionListener(e -> {
            dispose();
            if (parent != null) parent.setVisible(true);
        });
    }

    // =====================================
    // LOAD DATA
    // =====================================
    private void loadUserData() {
        tableModel.setRowCount(0);

        List<User> list = userDao.findAll();
        for (User u : list) {
            tableModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getFullName(),
                    u.getUsername(),
                    u.getRole()
            });
        }

        tableUser.revalidate();
        tableUser.repaint();
    }

    private void filter() {
        String kw = txtSearch.getText().trim().toLowerCase();

        tableModel.setRowCount(0);

        List<User> list = userDao.findAll();
        for (User u : list) {
            if (u.getFullName().toLowerCase().contains(kw)
                    || u.getUsername().toLowerCase().contains(kw)) {

                tableModel.addRow(new Object[]{
                        u.getUserId(),
                        u.getFullName(),
                        u.getUsername(),
                        u.getRole()
                });
            }
        }
    }

    private void loadToForm(int id) {
        User u = userDao.findById(id);
        if (u == null) return;

        currentUserId = u.getUserId();
        txtFullName.setText(u.getFullName());
        txtUsername.setText(u.getUsername());
        txtPassword.setText("");
        cmbRole.setSelectedItem(u.getRole());
    }

    private void resetForm() {
        currentUserId = null;
        txtFullName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
    }

    // =====================================
    // CRUD
    // =====================================
    private void addUser() {
        if (txtFullName.getText().isEmpty()
                || txtUsername.getText().isEmpty()
                || txtPassword.getPassword().length == 0) {

            JOptionPane.showMessageDialog(this, "Lengkapi semua data!");
            return;
        }

        User u = new User();
        u.setFullName(txtFullName.getText());
        u.setUsername(txtUsername.getText());
        u.setPassword(new String(txtPassword.getPassword()));
        u.setRole((String) cmbRole.getSelectedItem());

        int id = userDao.insert(u);

        if (id > 0) {
            JOptionPane.showMessageDialog(this, "User berhasil ditambah!");
            resetForm();
            loadUserData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambah user.");
        }
    }

    private void updateUser() {
        User u = new User();
        u.setUserId(currentUserId);
        u.setFullName(txtFullName.getText());
        u.setUsername(txtUsername.getText());
        u.setPassword(new String(txtPassword.getPassword()));
        u.setRole((String) cmbRole.getSelectedItem());

        boolean ok = userDao.update(u);

        if (ok) {
            JOptionPane.showMessageDialog(this, "User berhasil diupdate.");
            loadUserData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal update user.");
        }
    }

    private void deleteUser() {
        if (currentUserId == null) {
            JOptionPane.showMessageDialog(this, "Pilih user dahulu.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin hapus user?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = userDao.delete(currentUserId);

            if (ok) {
                JOptionPane.showMessageDialog(this, "User dihapus.");
                resetForm();
                loadUserData();
            }
        }
    }

    // Untuk testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserManagementFrame().setVisible(true));
    }
}
