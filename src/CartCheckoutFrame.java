import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal; // Import BigDecimal
import java.util.Date;

public class CartCheckoutFrame extends JFrame {

    private User customer; // Diganti menjadi User agar sesuai dengan Login
    private Cart cart;
    private JTable tableCart;
    private JLabel lblTotal;

    public CartCheckoutFrame(User customer, Cart cart) {
        this.customer = customer;
        this.cart = cart;

        setTitle("Checkout Pesanan");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Gunakan DISPOSE agar aplikasi tidak mati total
        setLayout(new BorderLayout());

        // Tabel Rincian
        DefaultTableModel model = new DefaultTableModel(new Object[] { "ID", "Menu", "Harga", "Qty", "Subtotal" }, 0);
        for (CartItem item : cart.getItems()) {
            model.addRow(new Object[] {
                    item.getMenuItem().getMenuId(),
                    item.getMenuItem().getName(),
                    item.getMenuItem().getPrice(),
                    item.getQuantity(),
                    item.getTotalPrice()
            });
        }

        tableCart = new JTable(model);
        add(new JScrollPane(tableCart), BorderLayout.CENTER);

        // Panel Bawah
        JPanel panelBottom = new JPanel(new GridLayout(2, 1));
        lblTotal = new JLabel("Total: Rp " + cart.getTotal());
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        panelBottom.add(lblTotal);

        JButton btnConfirm = new JButton("Konfirmasi Pesanan");
        btnConfirm.addActionListener(e -> confirmOrder());
        panelBottom.add(btnConfirm);

        add(panelBottom, BorderLayout.SOUTH);
    }

    private void confirmOrder() {
        try {
            // FIX ERROR: Konversi double ke BigDecimal menggunakan BigDecimal.valueOf()
            BigDecimal totalAmount = BigDecimal.valueOf(cart.getTotal());

            // Gunakan user_id untuk customer
            Order order = new Order(customer.getUserId(), totalAmount, new Date());

            OrderDAO dao = new OrderDAO();
            int orderId = dao.createOrder(order);

            for (CartItem item : cart.getItems()) {
                // FIX ERROR: Hapus BigDecimal.valueOf() karena getPrice() sudah BigDecimal
                // Sebelumnya: BigDecimal.valueOf(item.getMenuItem().getPrice()); -> Error
                BigDecimal itemPrice = item.getMenuItem().getPrice();
                BigDecimal subtotal = itemPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

                OrderDetail detail = new OrderDetail(orderId,
                        item.getMenuItem().getMenuId(),
                        item.getQuantity(),
                        subtotal); // Pass subtotal, because OrderDAO maps it to 'subtotal' column

                dao.addOrderDetail(detail);
            }

            JOptionPane.showMessageDialog(this, "Pesanan berhasil!");
            cart.clear(); // Kosongkan keranjang

            // Kembali ke dashboard - convert User ke Customer
            Customer customerObj = new Customer(customer.getUserId(), customer.getUsername(), customer.getPassword(),
                    customer.getFullName());
            new CustomerDashboard(customerObj, cart).setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal memproses pesanan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}