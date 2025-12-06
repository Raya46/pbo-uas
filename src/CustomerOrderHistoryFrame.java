import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerOrderHistoryFrame extends JFrame {

    private Customer customer;
    private JTable tableOrders;
    private DefaultTableModel model;

    public CustomerOrderHistoryFrame(Customer customer){
        this.customer = customer;

        setTitle("Riwayat Pesanan");
        setSize(800,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"OrderID","Tanggal","Total Harga","Detail"},0);
        tableOrders = new JTable(model);

        loadOrderHistory();

        add(new JScrollPane(tableOrders), BorderLayout.CENTER);

        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> {
            new CustomerDashboard(customer).setVisible(true);
            dispose();
        });
        add(btnBack, BorderLayout.SOUTH);
    }

    private void loadOrderHistory(){
        model.setRowCount(0);
        OrderDAO dao = new OrderDAO();
        List<Order> orders = dao.getOrdersByCustomer(customer.getCustomerId());

        for(Order order: orders){
            List<OrderDetail> details = dao.getOrderDetails(order.getId());
            StringBuilder sb = new StringBuilder();
            for(OrderDetail d: details){
                sb.append("MenuID: ").append(d.getMenuId())
                        .append(" x").append(d.getQuantity())
                        .append(" | ");
            }

            model.addRow(new Object[]{
                    order.getId(),
                    order.getOrderDate(),
                    order.getTotalPrice(),
                    sb.toString()
            });
        }
    }
}