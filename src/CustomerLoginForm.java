import javax.swing.*;
import java.awt.*;

public class CustomerLoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel;

    public CustomerLoginForm() {
        setTitle("Customer Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> loginCustomer());

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> System.exit(0));

        panel.add(btnLogin);
        panel.add(btnCancel);

        add(panel, BorderLayout.CENTER);
    }

    private void loginCustomer() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        CustomerDAO dao = new CustomerDAO();
        Customer customer = dao.login(username, password);

        if (customer != null) {
            JOptionPane.showMessageDialog(this, "Login berhasil! Selamat datang " + customer.getFullName());
            dispose();
            new CustomerDashboard(customer).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerLoginForm().setVisible(true));
    }
}