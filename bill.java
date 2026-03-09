package final_project2;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
   
    public class bill extends JFrame {
    public bill(String summary) {
        
        setTitle(" RECEIPT");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/folder/bill.jpeg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(400, 650, Image.SCALE_SMOOTH);
        ImageIcon backgroundIcon = new ImageIcon(scaledImage);

        
        JLabel background = new JLabel(backgroundIcon);
        background.setBounds(0, 0, 400, 650);

        
        int orderNumber = (int)(Math.random() * 90000) + 10000;
        JLabel message = new JLabel("Oreder number: "+orderNumber);
        
        message.setBounds(25, 103, 300, 30);
        message.setFont(new Font("Times New Roman", Font.BOLD, 18));
        message.setForeground(Color.BLACK);
        
        int otpNumber = (int)(Math.random() * 900) + 100;
        JLabel message2 = new JLabel("Pickup code: "+otpNumber);
        
        message2.setBounds(25, 127, 300, 30);
        message2.setFont(new Font("Times New Roman", Font.BOLD, 18));
        message2.setForeground(Color.BLACK);

        JTextArea message3 = new JTextArea(summary);
        message3.setBounds(25, 190, 340, 350);
        message3.setFont(new Font("Times New Roman", Font.BOLD, 16));
        message3.setForeground(Color.BLACK);
        message3.setOpaque(false);
        message3.setEditable(false);
        message3.setLineWrap(true);
        message3.setWrapStyleWord(true);
        
       
        
        background.setLayout(null);
        background.add(message);
        background.add(message2);
        background.add(message3);

        
        setContentPane(background);
        setVisible(true);
        setAlwaysOnTop(true); 
        
         SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showOptionDialog(
                this,
                "Do you want to save the receipt?",
                "Save Receipt",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Save", "Skip"},
                "Save"
            );

            if (choice == JOptionPane.YES_OPTION) {
                saveReceiptToFile(summary, orderNumber, otpNumber);
            }
        });
    }
     private void saveReceiptToFile(String summary, int orderNumber, int otpNumber) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Receipt");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile() + ".txt")) {
                writer.write("Order number: " + orderNumber + "\n");
                writer.write("Pickup code: " + otpNumber + "\n\n");
                writer.write(summary);
                JOptionPane.showMessageDialog(this, "Receipt saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving receipt: " + e.getMessage());
            }
        }
    
    }}