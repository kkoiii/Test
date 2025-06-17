package Model;

import java.io.Serializable;
import java.time.LocalDate;

public class Order implements Serializable {
    private static int nextId = 1;
    private int orderId;
    private String customerCode;
    private String menuCode;
    private int numberOfTables;
    private LocalDate eventDate;
    private double price;

    public Order(String customerCode, String menuCode, int numberOfTables, LocalDate eventDate, double price) {
        this.orderId = nextId++;
        this.customerCode = customerCode;
        this.menuCode = menuCode;
        this.numberOfTables = numberOfTables;
        this.eventDate = eventDate;
        this.price = price;
    }

    // Getters
    public int getOrderId() { return orderId; }
    public String getCustomerCode() { return customerCode; }
    public String getMenuCode() { return menuCode; }
    public int getNumberOfTables() { return numberOfTables; }
    public LocalDate getEventDate() { return eventDate; }
    public double getPrice() { return price; }
    public double getTotalCost() { return price * numberOfTables; }

    // Setters
    public void setMenuCode(String menuCode) { this.menuCode = menuCode; }
    public void setNumberOfTables(int numberOfTables) { this.numberOfTables = numberOfTables; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public void setPrice(double price) { this.price = price; }
} 