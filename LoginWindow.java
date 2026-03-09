package final_project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginWindow extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginWindow() {
        setTitle("OrderPick Mobile - Login");
        setSize(360, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(AppColors.BEIGE);

        this.usernameField = createTextField("Username / Phone number");
        this.passwordField = createPasswordField("Password");

        JLayeredPane mainLayeredPane = new JLayeredPane();
        mainLayeredPane.setPreferredSize(new Dimension(360, 640));

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(245, 245, 245));
                for (int i = 0; i < getWidth(); i += 20) {
                    for (int j = 0; j < getHeight(); j += 20) {
                        g2d.fillOval(i, j, 2, 2);
                    }
                }
            }
        };
        backgroundPanel.setBounds(0, 0, 360, 640);
        backgroundPanel.setBackground(AppColors.BEIGE);

        JPanel cardPanel = new JPanel();
        cardPanel.setBounds(30, 80, 300, 400);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel logoLabel = new JLabel("PICKUP");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setForeground(AppColors.RED);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(new Color(120, 120, 120));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = createButton("LOGIN", AppColors.RED);
        loginButton.addActionListener(this::handleLogin);

        JLabel forgotPassword = new JLabel("Forgot password?");
        forgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPassword.setForeground(new Color(150, 150, 150));
        forgotPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginWindow.this,
                        "Please contact support to reset your password",
                        "Forgot Password",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                forgotPassword.setForeground(AppColors.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgotPassword.setForeground(new Color(150, 150, 150));
            }
        });

        JLabel registerLabel = new JLabel("Don't have an account? Register");
        registerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerLabel.setForeground(new Color(150, 150, 150));
        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterWindow().setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registerLabel.setForeground(AppColors.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerLabel.setForeground(new Color(150, 150, 150));
            }
        });

        cardPanel.add(logoLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(welcomeLabel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(usernameField);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(passwordField);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(loginButton);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(forgotPassword);
        cardPanel.add(registerLabel);

        mainLayeredPane.add(backgroundPanel, Integer.valueOf(0));
        mainLayeredPane.add(cardPanel, Integer.valueOf(1));

        add(mainLayeredPane);
    }

    private void handleLogin(ActionEvent e) {
        try {
            performLogin();
        } catch (LoginFailedException ex) {
            showErrorDialog(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println("Driver not found: " + ex.getMessage());
            showErrorDialog("Application error. JDBC driver not found.");
        } catch (SQLException ex) {
            System.err.println("SQL error: " + ex.getMessage());
            showErrorDialog("Database connection failed. Please try again.");
        }
    }

    private void performLogin() throws LoginFailedException, SQLException, ClassNotFoundException {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            throw new LoginFailedException("Please enter both username/email/phone and password");
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project", "root", "Raha2099")) {
            String adminQuery = "SELECT * FROM admin WHERE (Username=? OR ContactInformation=?) AND Password=?";
            PreparedStatement adminStmt = conn.prepareStatement(adminQuery);
            adminStmt.setString(1, username);
            adminStmt.setString(2, username);
            adminStmt.setString(3, password);
            ResultSet adminResult = adminStmt.executeQuery();
            
            if (adminResult.next()) {
                new AdminDashboard().setVisible(true);
                dispose();
                conn.close();
                return;
            }
            
            String customerQuery = "SELECT * FROM customer WHERE (Username=? OR Email=? OR phoneNumber=?) AND Password=?";
            PreparedStatement custStmt = conn.prepareStatement(customerQuery);
            custStmt.setString(1, username);
            custStmt.setString(2, username);
            custStmt.setString(3, username);
            custStmt.setString(4, password);
            ResultSet custResult = custStmt.executeQuery();
            
            if (custResult.next()) {
                new CustomerDashboard().setVisible(true);
                dispose();
                conn.close();
                return;
            }
        }
        throw new LoginFailedException("Invalid credentials. Please try again.");
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(240, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 5, 8, 5)));
        field.setOpaque(false);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, AppColors.RED),
                        BorderFactory.createEmptyBorder(8, 5, 7, 5)));
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(8, 5, 8, 5)));
            }
        });
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.setMaximumSize(new Dimension(240, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 5, 8, 5)));
        field.setOpaque(false);
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('•');
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, AppColors.RED),
                        BorderFactory.createEmptyBorder(8, 5, 7, 5)));
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (field.getPassword().length == 0) {
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(8, 5, 8, 5)));
            }
        });
        return field;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(240, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" + message + "</div></html>",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
    }
}