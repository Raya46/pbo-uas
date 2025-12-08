import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MenuManagementFrame extends JFrame {

    // ========== NORTH ==========
    private JTextField txtSearch;
    private JComboBox<Category> cmbFilterCategory;

    // ========== CENTER ==========
    private JTable tableMenu;
    private DefaultTableModel tableModel;

    // ========== SOUTH FORM ==========
    private JTextField txtName;
    private JTextArea txtDescription;
    private JTextField txtPrice;
    private JSpinner spnStock;
    private JRadioButton rbAvailable;
    private JRadioButton rbUnavailable;
    private JComboBox<Category> cmbFormCategory;

    // ==== IMAGE FIELD ====
    private JButton btnBrowseImage;
    private JLabel lblImagePreview;
    private String selectedImagePath = null;

    // Buttons
    private JButton btnReset;
    private JButton btnAddNew;
    private JButton btnSave;
    private JButton btnDelete;

    // DAO
    private final MenuItemDAO menuDao = new MenuItemDAO();
    private final CategoryDAO categoryDao = new CategoryDAO();

    // state
    private Integer currentMenuId = null;

    // Formatter Indonesia
    private final NumberFormat numberFormat =
            NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));

    private JFrame parentFrame;


    public MenuManagementFrame(JFrame parent) {
        super("Manajemen Menu");
        this.parentFrame = parent;

        initComponents();
        loadCategories();
        loadMenuData(null, null);
        initActions();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (parentFrame != null) parentFrame.setVisible(true);
            }
        });

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public MenuManagementFrame() {
        this(null);
    }

    // =====================================================================================
    // INIT COMPONENTS
    // =====================================================================================
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // ---------- NORTH ----------
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtSearch = new JTextField(30);
        cmbFilterCategory = new JComboBox<>();

        northPanel.add(new JLabel("🔍"));
        northPanel.add(txtSearch);
        northPanel.add(new JLabel("Filter:"));
        northPanel.add(cmbFilterCategory);

        add(northPanel, BorderLayout.NORTH);

        // ---------- CENTER (TABLE) ----------
        String[] cols = {"ID", "Nama Menu", "Kategori", "Harga (Rp)", "Stok", "Status"};

        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tableMenu = new JTable(tableModel);
        tableMenu.setRowHeight(28);

        JTableHeader header = tableMenu.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setOpaque(true);
        header.setBackground(new Color(0, 51, 102));
        header.setForeground(Color.BLACK);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private final Color even = Color.WHITE;
            private final Color odd = new Color(240, 240, 240);

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(new Color(0, 120, 215));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? even : odd);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };

        for (int i = 0; i < tableMenu.getColumnCount(); i++) {
            tableMenu.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        tableWrapper.add(new JScrollPane(tableMenu), BorderLayout.CENTER);

        add(tableWrapper, BorderLayout.CENTER);


        // ---------- SOUTH (FORM) ----------
        JPanel southOuter = new JPanel(new BorderLayout());
        southOuter.setBorder(BorderFactory.createTitledBorder("Form Input"));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ==== Row 0: Nama + Kategori ====
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Nama Menu:"), gbc);

        gbc.gridx = 1;
        txtName = new JTextField();
        form.add(txtName, gbc);

        gbc.gridx = 2;
        form.add(new JLabel("Kategori:"), gbc);

        gbc.gridx = 3;
        cmbFormCategory = new JComboBox<>();
        form.add(cmbFormCategory, gbc);

        // ==== Row 1: Harga + Stok ====
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Harga (Rp):"), gbc);

        gbc.gridx = 1;
        txtPrice = new JTextField();
        form.add(txtPrice, gbc);

        gbc.gridx = 2;
        form.add(new JLabel("Stok:"), gbc);

        gbc.gridx = 3;
        spnStock = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
        form.add(spnStock, gbc);

        // ==== Row 2: Status ====
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        JPanel pStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbAvailable = new JRadioButton("Tersedia");
        rbUnavailable = new JRadioButton("Habis");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbAvailable); bg.add(rbUnavailable);
        rbAvailable.setSelected(true);
        pStatus.add(rbAvailable);
        pStatus.add(rbUnavailable);
        form.add(pStatus, gbc);

        // ==== Row 3: Deskripsi ====
        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Deskripsi:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        txtDescription = new JTextArea(3, 40);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        form.add(new JScrollPane(txtDescription), gbc);
        gbc.gridwidth = 1;

        // ==== Row 4: IMAGE PICKER ====
        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Gambar:"), gbc);

        gbc.gridx = 1;
        btnBrowseImage = new JButton("📁 Pilih Gambar");
        form.add(btnBrowseImage, gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        lblImagePreview = new JLabel();
        lblImagePreview.setPreferredSize(new Dimension(120, 90));
        lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
        form.add(lblImagePreview, gbc);
        gbc.gridwidth = 1;

        southOuter.add(form, BorderLayout.CENTER);


        add(southOuter, BorderLayout.SOUTH);

        // =====================================================================================
        // BUTTONS AREA
        // =====================================================================================
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnReset = new JButton("🔄 Reset");
        btnAddNew = new JButton("➕ Tambah");
        btnSave = new JButton("✏️ Simpan Edit");
        btnDelete = new JButton("🗑️ Hapus");

        btnAddNew.setBackground(new Color(0, 128, 64));
        btnAddNew.setForeground(Color.BLACK);

        btnSave.setBackground(new Color(0, 70, 140));
        btnSave.setForeground(Color.BLACK);

        btnDelete.setBackground(new Color(160, 0, 0));
        btnDelete.setForeground(Color.BLACK);

        for (JButton b : new JButton[]{btnReset, btnAddNew, btnSave, btnDelete}) {
            b.setPreferredSize(new Dimension(130, 34));
            b.setFocusPainted(false);
            actionPanel.add(b);
        }

        southOuter.add(actionPanel, BorderLayout.SOUTH);
        add(southOuter, BorderLayout.SOUTH);
    }

    // =====================================================================================
    // LOAD CATEGORY
    // =====================================================================================
    private void loadCategories() {
        cmbFilterCategory.removeAllItems();
        cmbFilterCategory.addItem(new Category(0, "Semua"));

        cmbFormCategory.removeAllItems();

        List<Category> list = categoryDao.findAll();
        for (Category c : list) {
            cmbFilterCategory.addItem(c);
            cmbFormCategory.addItem(c);
        }
    }

    // =====================================================================================
    // ACTIONS
    // =====================================================================================
    private void initActions() {

        // search
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshFilter(); }
            public void removeUpdate(DocumentEvent e) { refreshFilter(); }
            public void changedUpdate(DocumentEvent e) { refreshFilter(); }
        });

        cmbFilterCategory.addActionListener(e -> refreshFilter());

        // Table selection → load form
        tableMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tableMenu.getSelectedRow();
                if (r >= 0) {
                    int id = (int) tableModel.getValueAt(r, 0);
                    loadMenuToForm(id);
                }
            }
        });

        // ====== IMAGE BROWSER ======
        btnBrowseImage.addActionListener(e -> pickImageFile());

        // RESET
        btnReset.addActionListener(e -> clearForm());

        // INSERT
        btnAddNew.addActionListener(e -> {
            currentMenuId = null;
            MenuItem m = collectFormData();
            if (m == null) return;

            int genId = menuDao.insert(m);
            if (genId > 0) {
                JOptionPane.showMessageDialog(this, "Menu berhasil ditambah!");
                clearForm();
                loadMenuData(null, null);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambah menu.");
            }
        });

        // UPDATE
        btnSave.addActionListener(e -> {
            if (currentMenuId == null) {
                JOptionPane.showMessageDialog(this, "Pilih menu dari tabel untuk diedit.");
                return;
            }

            MenuItem m = collectFormData();
            if (m == null) return;

            boolean ok = menuDao.update(m);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Perubahan disimpan.");
                loadMenuData(null, null);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal update menu.");
            }
        });

        // DELETE
        btnDelete.addActionListener(e -> {
            if (currentMenuId == null) {
                JOptionPane.showMessageDialog(this, "Pilih menu dahulu.");
                return;
            }

            int c = JOptionPane.showConfirmDialog(
                    this, "Yakin hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (c == JOptionPane.YES_OPTION) {
                boolean ok = menuDao.delete(currentMenuId);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Menu dihapus.");
                    clearForm();
                    loadMenuData(null, null);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus.");
                }
            }
        });
    }

    // =====================================================================================
    // IMAGE PICKER
    // =====================================================================================
    private void pickImageFile() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Pilih Gambar Menu");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif"
        ));

        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            selectedImagePath = f.getAbsolutePath();
            showImagePreview(selectedImagePath);
        }
    }

    private void showImagePreview(String path) {
        if (path == null || path.isEmpty()) {
            lblImagePreview.setIcon(null);
            return;
        }

        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(
                lblImagePreview.getWidth(),
                lblImagePreview.getHeight(),
                Image.SCALE_SMOOTH
        );

        lblImagePreview.setIcon(new ImageIcon(img));
    }

    // =====================================================================================
    // FILTER
    // =====================================================================================
    private void refreshFilter() {
        String kw = txtSearch.getText().trim();
        Category c = (Category) cmbFilterCategory.getSelectedItem();

        Integer cid = (c != null && c.getCategoryId() != 0) ? c.getCategoryId() : null;

        loadMenuData(kw.isEmpty() ? null : kw, cid);
    }

    private void loadMenuData(String keyword, Integer categoryId) {
        tableModel.setRowCount(0);

        List<MenuItem> list = menuDao.findAll();

        for (MenuItem m : list) {

            boolean ok = true;

            if (keyword != null) {
                String k = keyword.toLowerCase();
                ok = m.getName().toLowerCase().contains(k)
                        || (m.getDescription() != null &&
                        m.getDescription().toLowerCase().contains(k));
            }

            if (ok && categoryId != null) {
                ok = m.getCategory() != null &&
                        m.getCategory().getCategoryId() == categoryId;
            }

            if (!ok) continue;

            tableModel.addRow(new Object[]{
                    m.getMenuId(),
                    m.getName(),
                    m.getCategory() != null ? m.getCategory().getCategoryName() : "-",
                    numberFormat.format(m.getPrice()),
                    m.getStock(),
                    m.isAvailable() ? "Tersedia" : "Habis"
            });
        }
    }
    // =====================================================================================
    // LOAD MENU INTO FORM (INCLUDING IMAGE) - FIXED
    // =====================================================================================
    private void loadMenuToForm(int id) {
        MenuItem m = menuDao.findById(id);
        if (m == null) return;

        currentMenuId = id;

        txtName.setText(m.getName());
        txtDescription.setText(m.getDescription());
        txtPrice.setText(m.getPrice().toPlainString());
        spnStock.setValue(m.getStock());
        rbAvailable.setSelected(m.isAvailable());
        rbUnavailable.setSelected(!m.isAvailable());

        // ========= SAFE CATEGORY SELECTION =========
        if (m.getCategory() != null) {

            boolean matchFound = false;

            for (int i = 0; i < cmbFormCategory.getItemCount(); i++) {
                Category item = cmbFormCategory.getItemAt(i);

                if (item != null && item.getCategoryId() == m.getCategory().getCategoryId()) {
                    cmbFormCategory.setSelectedIndex(i);
                    matchFound = true;
                    break;
                }
            }

            if (!matchFound && cmbFormCategory.getItemCount() > 0) {
                cmbFormCategory.setSelectedIndex(0);
            }

        } else {
            if (cmbFormCategory.getItemCount() > 0) {
                cmbFormCategory.setSelectedIndex(0);
            }
        }

        // ========= LOAD IMAGE SAFELY =========
        selectedImagePath = m.getImagePath();

        if (selectedImagePath != null && !selectedImagePath.isBlank() && new File(selectedImagePath).exists()) {
            showImagePreview(selectedImagePath);
        } else {
            selectedImagePath = null;
            showImagePreview(null);
        }
    }

    // =====================================================================================
    // COLLECT FORM DATA (INCLUDING IMAGE PATH)
    // =====================================================================================
    private MenuItem collectFormData() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama wajib diisi.");
            return null;
        }
        if (txtPrice.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harga wajib diisi.");
            return null;
        }

        BigDecimal price;
        try {
            price = parsePrice(txtPrice.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format harga salah.");
            return null;
        }

        Category cat = (Category) cmbFormCategory.getSelectedItem();
        if (cat == null) {
            JOptionPane.showMessageDialog(this, "Pilih kategori.");
            return null;
        }

        MenuItem m = new MenuItem();

        if (currentMenuId != null) {
            m.setMenuId(currentMenuId);
        }

        m.setName(txtName.getText().trim());
        m.setDescription(txtDescription.getText());
        m.setPrice(price);
        m.setStock((Integer) spnStock.getValue());
        m.setAvailable(rbAvailable.isSelected());
        m.setCategory(cat);
        m.setImagePath(selectedImagePath);

        return m;
    }


    // =====================================================================================
    // CLEAR FORM (RESET IMAGE TOO)
    // =====================================================================================
    private void clearForm() {
        currentMenuId = null;

        txtName.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        spnStock.setValue(0);
        rbAvailable.setSelected(true);

        if (cmbFormCategory.getItemCount() > 0)
            cmbFormCategory.setSelectedIndex(0);

        // reset gambar
        selectedImagePath = null;
        lblImagePreview.setIcon(null);
    }

    // =====================================================================================
    // PRICE PARSER
    // =====================================================================================
    private BigDecimal parsePrice(String s) {
        s = s.replace(".", "").replace(",", ".");
        return new BigDecimal(s);
    }

    // =====================================================================================
    // MAIN
    // =====================================================================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new MenuManagementFrame().setVisible(true));
    }
}

