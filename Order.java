package final_project2;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private Customer customer;
    private List<MenuItem> items;
    private String status;

    public Order(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.status = "not paid";  // Set default status
    }

    public String getStatus() {
        return status;
    }

    public void markAsPaid() {
        this.status = "paid";
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }
}
