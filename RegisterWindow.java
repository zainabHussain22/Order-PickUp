package final_project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterWindow extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterWindow() {
        setTitle("Register");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(AppColors.BEIGE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 30, 30));

        JLabel header = new JLabel("Create Account");
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(AppColors.RED);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(header);
        mainPanel.add(Box.createVerticalStrut(30));

        nameField = createStyledTextField("Name");
        emailField = createStyledTextField("Email");
        phoneField = createStyledTextField("Phone");
        passwordField = createStyledPasswordField("Password");

        registerButton = new JButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerButton.setBackground(AppColors.RED);
        registerButton.setForeground(AppColors.TEXT_ON_RED);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setMaximumSize(new Dimension(240, 45));

        mainPanel.add(nameField);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(emailField);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(phoneField);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(registerButton);

        add(mainPanel);

        registerButton.addActionListener(e -> registerUser());
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(280, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, AppColors.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setBackground(AppColors.BEIGE);
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(280, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, AppColors.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setBackground(AppColors.BEIGE);
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('•');
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setText(placeholder);
                    field.setEchoChar((char) 0);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        return field;
    }

    private void registerUser() {
        String username = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        try {
            String url = "jdbc:mysql://localhost:3306/project";
            String dbUser = "root";
            String dbPass = "Raha2099" ;

            Connection conn = DriverManager.getConnection(url, dbUser, dbPass);

            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM customer WHERE Username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                showError("Username already exists.");
                conn.close();
                return;
            }

            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO customer (Username, Password, Email, phoneNumber) VALUES (?, ?, ?, ?)"
            );
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, email);
            insertStmt.setString(4, phone);

            int rows = insertStmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Registered successfully!");
                new LoginWindow().setVisible(true);
                dispose();
            } else {
                showError("Something went wrong.");
            }

            conn.close();

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}