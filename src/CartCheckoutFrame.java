import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class CartCheckoutFrame extends JFrame {

    private Customer customer;
    private Cart cart;
    private JTable tableCart;
    private JLabel lblTotal;

    public CartCheckoutFrame(Customer customer, Cart cart){
        this.customer = customer;
        this.cart = cart;

        setTitle("Checkout Pesanan");
        setSize(600,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Menu","Harga","Qty","Subtotal"},0);
        for(CartItem item: cart.getItems()){
            model.addRow(new Object[]{
                    item.getMenuItem().getMenuId(),
                    item.getMenuItem().getName(),
                    item.getMenuItem().getPrice(),
                    item.getQuantity(),
                    item.getTotalPrice()
            });
        }

        tableCart = new JTable(model);
        add(new JScrollPane(tableCart), BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new GridLayout(2,1));
        lblTotal = new JLabel("Total: Rp "+cart.getTotal());
        lblTotal.setFont(new Font("Arial",Font.BOLD,18));
        panelBottom.add(lblTotal);

        JButton btnConfirm = new JButton("Konfirmasi Pesanan");
        btnConfirm.addActionListener(e -> confirmOrder());
        panelBottom.add(btnConfirm);

        add(panelBottom, BorderLayout.SOUTH);
    }

    private void confirmOrder(){
        try{
            Order order = new Order(customer.getCustomerId(), cart.getTotal(), new Date());
            OrderDAO dao = new OrderDAO();
            int orderId = dao.createOrder(order);

            for(CartItem item: cart.getItems()){
                OrderDetail detail = new OrderDetail(orderId,
                        item.getMenuItem().getMenuId(),
                        item.getQuantity(),
                        item.getMenuItem().getPrice());
                dao.addOrderDetail(detail);
            }

            JOptionPane.showMessageDialog(this,"Pesanan berhasil!");
            cart.clear();
            new CustomerDashboard(customer, cart).setVisible(true);
            dispose();

        } catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Gagal memproses pesanan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}