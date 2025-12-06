import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CustomerViewMenuFrame extends JFrame {

    private Customer customer;
    private Cart cart;

    private JTable tableMenu;
    private DefaultTableModel model;
    private JButton btnAddToCart, btnViewCart, btnBack;

    public CustomerViewMenuFrame(Customer customer, Cart cart) {
        this.customer = customer;
        this.cart = cart;

        setTitle("Daftar Menu");
        setSize(700,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        model = new DefaultTableModel(new Object[]{"ID","Nama Menu","Harga"},0);
        tableMenu = new JTable(model);
        loadDataMenu();

        btnAddToCart = new JButton("Tambah ke Keranjang");
        btnViewCart = new JButton("Lihat Keranjang");
        btnBack = new JButton("Kembali");

        JPanel panelBottom = new JPanel();
        panelBottom.add(btnAddToCart);
        panelBottom.add(btnViewCart);
        panelBottom.add(btnBack);

        add(new JScrollPane(tableMenu), BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        btnAddToCart.addActionListener(e -> {
            int selectedRow = tableMenu.getSelectedRow();
            if(selectedRow>=0){
                int menuId = (int) tableMenu.getValueAt(selectedRow,0);
                String name = (String) tableMenu.getValueAt(selectedRow,1);
                double price = (double) tableMenu.getValueAt(selectedRow,2);

                MenuItem menuItem = new MenuItem(menuId,name,price);
                cart.addItem(menuItem,1);

                JOptionPane.showMessageDialog(this,"Berhasil ditambahkan ke keranjang!");
            } else {
                JOptionPane.showMessageDialog(this,"Pilih menu terlebih dahulu!");
            }
        });

        btnViewCart.addActionListener(e -> {
            new CustomerCartFrame(customer,cart).setVisible(true);
            dispose();
        });

        btnBack.addActionListener(e -> {
            new CustomerDashboard(customer, cart).setVisible(true);
            dispose();
        });
    }

    private void loadDataMenu(){
        try{
            MenuDAO dao = new MenuDAO();
            ArrayList<MenuItem> menuList = dao.getAllMenuItems();
            model.setRowCount(0);
            for(MenuItem m: menuList){
                model.addRow(new Object[]{m.getMenuId(), m.getName(), m.getPrice()});
            }
        } catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Gagal memuat data menu!");
            ex.printStackTrace();
        }
    }
}