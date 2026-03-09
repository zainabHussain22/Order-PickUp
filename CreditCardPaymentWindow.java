package final_project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreditCardPaymentWindow extends JFrame {

    private JTextField cardNumberField;
    private JTextField expDateField;
    private JTextField cardHolderField;
    private JTextField cvvField;
    private String orderSummary;

    public CreditCardPaymentWindow(String orderSummary) {
        this.orderSummary = orderSummary;

        setTitle("Credit Card Payment");
        setSize(360, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(AppColors.BEIGE);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberField = createTextField();

        JLabel expDateLabel = new JLabel("Expiry Date (MM/YY):");
        expDateField = createTextField();

        JLabel cardHolderLabel = new JLabel("Card Holder Name:");
        cardHolderField = createTextField();

        JLabel cvvLabel = new JLabel("CVV:");
        cvvField = createTextField();

        JButton payButton = createButton("Pay Now", AppColors.RED);
        payButton.addActionListener(e -> processPayment());

        cardPanel.add(cardNumberLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(cardNumberField);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(expDateLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(expDateField);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(cardHolderLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(cardHolderField);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(cvvLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(cvvField);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(payButton);

        setLayout(new GridBagLayout());
        add(cardPanel);

        setVisible(true);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(260, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setOpaque(false);
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
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void processPayment() {
        String cardNumber = cardNumberField.getText().trim();
        String expDate = expDateField.getText().trim();
        String cardHolder = cardHolderField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (!cardNumber.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "Card Number must be exactly 16 digits.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth exp = YearMonth.parse(expDate, formatter);
            YearMonth now = YearMonth.now();
            if (exp.isBefore(now)) {
                JOptionPane.showMessageDialog(this, "Card is expired. Please use a valid card.");
                return;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Expiry date must be in MM/YY format.");
            return;
        }

        if (!cardHolder.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(this, "Card Holder Name must contain letters only.");
            return;
        }

        if (!cvv.matches("\\d{3}")) {
            JOptionPane.showMessageDialog(this, "CVV must be exactly 3 digits.");
            return;
        }

        dispose();
        new bill(orderSummary);
    }
}