import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerCartFrame extends JFrame {

    private Customer customer;
    private Cart cart;
    private JTable tableCart;
    private DefaultTableModel model;
    private JButton btnCheckout, btnBack;

    public CustomerCartFrame(Customer customer, Cart cart){
        this.customer = customer;
        this.cart = cart;

        setTitle("Keranjang Pesanan");
        setSize(700,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        model = new DefaultTableModel(new Object[]{"ID","Nama Menu","Qty","Subtotal"},0);
        tableCart = new JTable(model);
        loadCartItems();

        btnCheckout = new JButton("Checkout");
        btnBack = new JButton("Kembali");

        JPanel panelBottom = new JPanel();
        panelBottom.add(btnCheckout);
        panelBottom.add(btnBack);

        add(new JScrollPane(tableCart), BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> {
            new CustomerViewMenuFrame(customer,cart).setVisible(true);
            dispose();
        });

        btnCheckout.addActionListener(e -> checkoutProcess());
    }

    private void loadCartItems(){
        model.setRowCount(0);
        for(CartItem item: cart.getItems()){
            model.addRow(new Object[]{
                    item.getMenuItem().getMenuId(),
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            });
        }
    }

    private void checkoutProcess(){
        if(cart.getItems().isEmpty()){
            JOptionPane.showMessageDialog(this,"Keranjang kosong!","Info",JOptionPane.WARNING_MESSAGE);
            return;
        }
        new CartCheckoutFrame(customer,cart).setVisible(true);
        dispose();
    }
}