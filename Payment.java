package final_project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Payment {
    private String paymentID;
    private int orderID;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentDate;

    public Payment(String paymentID, int orderID, double amount,
                   String paymentMethod, String paymentDate) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.paymentStatus = "Not Paid";
    }

    public void processPayment() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "Raha2099")) {
            // Insert into payment table
            PreparedStatement paymentStmt = conn.prepareStatement(
                "INSERT INTO payment (PaymentID, OrderID, Amount, Method, Status, Date) VALUES (?, ?, ?, ?, ?, ?)");
            paymentStmt.setString(1, paymentID);
            paymentStmt.setInt(2, orderID);
            paymentStmt.setDouble(3, amount);
            paymentStmt.setString(4, paymentMethod);
            paymentStmt.setString(5, "Paid"); // Only status used now
            paymentStmt.setString(6, paymentDate);
            paymentStmt.executeUpdate();

            // Update order status
            PreparedStatement updateStmt = conn.prepareStatement(
                "UPDATE order SET Status = 'Paid' WHERE OrderID = ?");
            updateStmt.setInt(1, orderID);
            updateStmt.executeUpdate();

            this.paymentStatus = "Paid";
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}