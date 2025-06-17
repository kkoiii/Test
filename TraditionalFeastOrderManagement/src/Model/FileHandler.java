package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String CUSTOMERS_FILE = "src/data/customers.dat";
    private static final String ORDERS_FILE = "src/data/feast_order_service.dat";
    private static final String MENUS_FILE = "src/data/feastMenu.csv";

    // Save customers to binary file
    public static void saveCustomers(List<Customer> customers) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CUSTOMERS_FILE))) {
            oos.writeObject(new ArrayList<>(customers));
            System.out.println("Customer data has been successfully saved to \"" + CUSTOMERS_FILE + "\"");
        }
    }

    // Save orders to binary file
    public static void saveOrders(List<Order> orders) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ORDERS_FILE))) {
            oos.writeObject(new ArrayList<>(orders));
            System.out.println("Order data has been successfully saved to \"" + ORDERS_FILE + "\"");
        }
    }

    // Load customers from binary file
    @SuppressWarnings("unchecked")
    public static List<Customer> loadCustomers() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CUSTOMERS_FILE))) {
            return (List<Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // Load orders from binary file
    @SuppressWarnings("unchecked")
    public static List<Order> loadOrders() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ORDERS_FILE))) {
            return (List<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // Load menus from CSV file (unchanged)
    public static List<Menu> loadMenus() {
        List<Menu> menus = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MENUS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    List<String> ingredients = new ArrayList<>();
                    for (int i = 3; i < parts.length; i++) {
                        ingredients.add(parts[i].trim());
                    }
                    menus.add(new Menu(code, name, price, ingredients));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading menus: " + e.getMessage());
        }
        return menus;
    }
} 