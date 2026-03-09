package final_project2;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class CustomerDashboard extends JFrame {
    private ArrayList<Map<String, Object>> menuItems = new ArrayList<>();
    private ArrayList<Map<String, Object>> cartItems = new ArrayList<>();
    private JPanel cartPanel;
    private JLabel totalLabel;

    public CustomerDashboard() {
        initializeMenu();
        setupUI();
    }

    private void setupUI() {
        setTitle("OrderPick - Menu");
        setSize(400, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel headerPanel = createHeaderPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Menu", createMenuPanel());
        tabbedPane.addTab("Cart (0)", createCartPanel());

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.RED);
        headerPanel.setPreferredSize(new Dimension(360, 60));

        JButton backButton = new JButton("← Back");
        styleHeaderButton(backButton);
        backButton.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });

        headerPanel.add(backButton, BorderLayout.WEST);
        return headerPanel;
    }

    private ImageIcon loadImageIconSafely(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) return new ImageIcon(url);
        } catch (Exception e) {
            System.err.println("Error loading icon at: " + path);
        }
        return null;
    }

    private JScrollPane createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(AppColors.BEIGE);

        LinkedHashMap<String, ArrayList<Map<String, Object>>> categorized = new LinkedHashMap<>();
        for (Map<String, Object> item : menuItems) {
            String category = (String) item.get("category");
            categorized.computeIfAbsent(category, k -> new ArrayList<>()).add(item);
        }

        for (Map.Entry<String, ArrayList<Map<String, Object>>> entry : categorized.entrySet()) {
            menuPanel.add(createCategoryHeader(entry.getKey()));
            menuPanel.add(Box.createVerticalStrut(5));
            for (Map<String, Object> item : entry.getValue()) {
                menuPanel.add(createMenuItemCard(item));
                menuPanel.add(Box.createVerticalStrut(10));
            }
        }

        return new JScrollPane(menuPanel);
    }

    private JPanel createCartPanel() {
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(AppColors.BEIGE);

        JScrollPane cartScrollPane = new JScrollPane(cartPanel);
        cartScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.add(cartScrollPane, BorderLayout.CENTER);

        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBackground(AppColors.BEIGE);
        checkoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalLabel = new JLabel("Total: SAR 0.00", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton checkoutButton = new JButton("Complete Order");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setBackground(AppColors.RED);
        checkoutButton.setForeground(AppColors.TEXT_ON_RED);
        checkoutButton.setBorderPainted(false);
        checkoutButton.addActionListener(e -> completeOrder());

        checkoutPanel.add(totalLabel, BorderLayout.CENTER);
        checkoutPanel.add(checkoutButton, BorderLayout.SOUTH);

        cartContainer.add(checkoutPanel, BorderLayout.SOUTH);
        return cartContainer;
    }

    private void initializeMenu() {
        addMenuItem("B1", "Cheeseburger", 20.0, "Burgers", "/folder/Cheeseburger.jpeg");
        addMenuItem("B2", "Double Cheeseburger", 25.0, "Burgers", "/folder/dCheeseburger.jpeg");
        addMenuItem("C1", "Chicken Sandwich", 18.0, "Chicken", "/folder/Chicken_Sandwich.jpeg");
        addMenuItem("S1", "Fries", 8.0, "Sides", "/folder/fries.jpeg");
        addMenuItem("S2", "Onion Rings", 9.0, "Sides", "/folder/onion_rings.jpeg");
        addMenuItem("S3", "Mozzarella Stick", 9.0, "Sides", "/folder/mozzarella_stick.jpeg");
        addMenuItem("C2", "Mac and Cheese", 18.0, "Sides", "/folder/Mac_and_cheese.jpeg");
        addMenuItem("D1", "Cola", 5.0, "Drinks", "/folder/cola.jpeg");
        addMenuItem("D2", "Fanta", 5.0, "Drinks", "/folder/fanta.jpeg");
        addMenuItem("D3", "Fresh Orange Juice", 7.0, "Drinks", "/folder/fresh_orange_juice.jpeg");
        addMenuItem("D4", "Water", 3.0, "Drinks", "/folder/water.jpeg");
        addMenuItem("SA1", "Honey Mustard Sauce", 3.0, "Sauces", "/folder/Honey_mustard_sauce.jpeg");
        addMenuItem("SA2", "Sweet Chili Sauce", 3.0, "Sauces", "/folder/Sweet_chili_sauce.jpeg");
    }

    public void addMenuItem(String code, String name, double price, String category, String imagePath) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", code);
        item.put("name", name);
        item.put("price", price);
        item.put("category", category);
        item.put("imagePath", imagePath);
        menuItems.add(item);
    }

    private JPanel createCategoryHeader(String category) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(getCategoryColor(category));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel label = new JLabel(category);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);

        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    private JPanel createMenuItemCard(Map<String, Object> item) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setPreferredSize(new Dimension(340, 80));
        card.setBackground(AppColors.WHITE);
        card.setBorder(BorderFactory.createLineBorder(AppColors.BORDER_COLOR));

        JLabel imageLabel = new JLabel("No Image");
        imageLabel.setPreferredSize(new Dimension(80, 80));

        try {
            ImageIcon rawIcon = loadImageIconSafely((String) item.get("imagePath"));
            if (rawIcon != null) {
                Image scaled = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
                imageLabel.setText("");
            }
        } catch (Exception e) {
            System.err.println("Error loading image for " + item.get("name"));
        }

        card.add(imageLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(AppColors.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel((String) item.get("name"));
        JLabel priceLabel = new JLabel(String.format("SAR %.2f", (double) item.get("price")));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setForeground(AppColors.RED);

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        JButton addButton = new JButton("+");
        styleAddButton(addButton, getCategoryColor((String) item.get("category")));
        addButton.addActionListener(e -> addToCart(item));

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.EAST);

        return card;
    }

    private void addToCart(Map<String, Object> item) {
        for (Map<String, Object> cartItem : cartItems) {
            if (cartItem.get("id").equals(item.get("id"))) {
                cartItem.put("quantity", (int) cartItem.getOrDefault("quantity", 1) + 1);
                updateCartDisplay();
                return;
            }
        }

        Map<String, Object> newItem = new HashMap<>(item);
        newItem.put("quantity", 1);
        cartItems.add(newItem);
        updateCartDisplay();
    }

    private void updateCartDisplay() {
        cartPanel.removeAll();

        for (Map<String, Object> item : cartItems) {
            cartPanel.add(createCartItemCard(item));
            cartPanel.add(Box.createVerticalStrut(10));
        }

        double total = cartItems.stream()
                .mapToDouble(i -> (double) i.get("price") * (int) i.get("quantity"))
                .sum();

        totalLabel.setText(String.format("Total: SAR %.2f", total));

        JTabbedPane tabs = (JTabbedPane) getContentPane().getComponent(1);
        tabs.setTitleAt(1, String.format("Cart (%d)", cartItems.size()));

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private JPanel createCartItemCard(Map<String, Object> item) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setPreferredSize(new Dimension(340, 60));
        card.setBackground(AppColors.WHITE);
        card.setBorder(BorderFactory.createLineBorder(AppColors.BORDER_COLOR));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(AppColors.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel name = new JLabel((String) item.get("name"));
        JLabel price = new JLabel(String.format("SAR %.2f x %d",
                item.get("price"), item.get("quantity")));

        infoPanel.add(name);
        infoPanel.add(price);

        JPanel qtyPanel = new JPanel(new BorderLayout());
        qtyPanel.setBackground(AppColors.WHITE);

        JButton minus = new JButton("-");
        JButton plus = new JButton("+");
        styleQuantityButton(minus);
        styleQuantityButton(plus);

        minus.addActionListener(e -> {
            int quantity = (int) item.get("quantity");
            if (quantity > 1) {
                item.put("quantity", quantity - 1);
            } else {
                cartItems.remove(item);
            }
            updateCartDisplay();
        });

        plus.addActionListener(e -> {
            item.put("quantity", (int) item.get("quantity") + 1);
            updateCartDisplay();
        });

        qtyPanel.add(minus, BorderLayout.WEST);
        qtyPanel.add(plus, BorderLayout.EAST);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(qtyPanel, BorderLayout.EAST);

        return card;
    }

    private void completeOrder() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] options = {"Cash", "Credit Card"};
        int choice = JOptionPane.showOptionDialog(this, "Choose payment method:", "Payment Method",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        String summary = generateOrderSummary("Your order will be ready for pickup in 15 minutes!");

        if (choice == 0) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Raha2099")) {
               PreparedStatement stmt = conn.prepareStatement(
                       "INSERT INTO `order` (CustomerID, Status, TotalCost) VALUES (?, ?, ?)");


                stmt.setInt(1, 1); // Replace with actual logged-in customer ID
                stmt.setString(2, "Not Paid");
                stmt.setDouble(3, calculateTotal());
                stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to save order to database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            new bill(summary);
        } else if (choice == 1) {
            new CreditCardPaymentWindow(summary);
        }

        cartItems.clear();
        updateCartDisplay();
    }

    private double calculateTotal() {
        return cartItems.stream()
                .mapToDouble(i -> (double) i.get("price") * (int) i.get("quantity"))
                .sum();
    }

    private Color getCategoryColor(String category) {
        return Color.GRAY;
    }

    private void styleHeaderButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(AppColors.RED);
        button.setForeground(AppColors.TEXT_ON_RED);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    private void styleAddButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(50, 50));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    private void styleQuantityButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(40, 30));
        button.setBackground(AppColors.RED);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }

    private String generateOrderSummary(String message) {
        StringBuilder sb = new StringBuilder(message).append("\n\nOrder Summary:\n");
        for (Map<String, Object> item : cartItems) {
            sb.append(String.format("- %s x%d: SAR %.2f\n",
                    item.get("name"), item.get("quantity"),
                    (double) item.get("price") * (int) item.get("quantity")));
        }
        sb.append(String.format("\nTotal: SAR %.2f", calculateTotal()));
        return sb.toString();
    }
}