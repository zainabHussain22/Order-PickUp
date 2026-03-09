package final_project2;

import javax.swing.ImageIcon;

public class MenuItem {
    private String id;
    private String name;
    private double price;
    private String category;
    private ImageIcon image;
    
    public MenuItem(String id, String name, double price, String category, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.image = new ImageIcon(getClass().getResource(imagePath));
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public ImageIcon getImage() { return image; }
}