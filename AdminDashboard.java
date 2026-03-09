package final_project2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("OrderPick - Admin Dashboard");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppColors.BEIGE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.RED);
        headerPanel.setPreferredSize(new Dimension(360, 60));

        JButton logoutButton = new JButton("\u2190 Logout");
        styleHeaderButton(logoutButton);
        logoutButton.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });

        JLabel titleLabel = new JLabel("ADMIN PANEL", SwingConstants.CENTER);
        titleLabel.setForeground(AppColors.TEXT_ON_RED);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        headerPanel.add(logoutButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel functionPanel = new JPanel();
        functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.Y_AXIS));
        functionPanel.setBackground(AppColors.BEIGE);
        functionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addButton(functionPanel, "View Menu List", e -> openMenuList());
        addButton(functionPanel, "View Orders", e -> openOrdersView());
        addButton(functionPanel, "Staff Information", e -> showStaffInfo());

        mainPanel.add(functionPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void addButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(260, 45));
        button.setPreferredSize(new Dimension(260, 45));
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBackground(AppColors.WHITE);
        button.setForeground(AppColors.RED);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(AppColors.RED, 2));
        button.addActionListener(action);
        panel.add(Box.createVerticalStrut(12));
        panel.add(button);
    }

    private void styleHeaderButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(AppColors.RED);
        button.setForeground(AppColors.TEXT_ON_RED);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private void openMenuList() {
        JFrame frame = new JFrame("Menu Items in Database");
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ItemID", "Name", "Price", "Category"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);
        loadMenuItems(model);
        frame.setVisible(true);
    }

    private void loadMenuItems(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/project", "root", "Raha2099")) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM menuitem");
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ItemID"),
                    rs.getString("Name"),
                    rs.getDouble("Price"),
                    rs.getString("Category")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
        }
    }

    private void openOrdersView() {
        JFrame frame = new JFrame("View Orders");
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Order ID", "Status", "Total", "Customer ID"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);
        loadOrders(model);
        frame.setVisible(true);
    }

    private void loadOrders(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/project", "root", "Raha2099")) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `order` ORDER BY OrderID DESC");
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("OrderID"),
                    rs.getString("Status"),
                    rs.getDouble("TotalCost"),
                    rs.getInt("CustomerID")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
        }
    }

    private void showStaffInfo() {
        JFrame frame = new JFrame("Staff Information");
        frame.setSize(450, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Admin ID", "Username", "Contact Info"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);

        try (Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/project", "root", "Raha2099" )) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT AdminID, Username, ContactInformation FROM admin");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("AdminID"),
                    rs.getString("Username"),
                    rs.getString("ContactInformation")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
        }

        frame.setVisible(true);
    }
}