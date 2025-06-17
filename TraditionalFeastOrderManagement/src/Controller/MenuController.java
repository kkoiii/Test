package Controller;

import Model.Customer;
import Model.Menu;
import Model.Order;
import Model.FileHandler;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class MenuController {
    private List<Customer> customers;
    private List<Order> orders;
    private List<Menu> menus;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MenuController() {
        customers = FileHandler.loadCustomers();
        orders = FileHandler.loadOrders();
        menus = FileHandler.loadMenus();
    }

    // Function 1: Register customers
    public void registerCustomer(String code, String name, String phone, String email) throws IllegalArgumentException {
        // Check if customer code already exists
        for (Customer c : customers) {
            if (c.getCustomerCode().equals(code)) {
                throw new IllegalArgumentException("Customer code already exists");
            }
        }
        
        Customer customer = new Customer(code, name, phone, email);
        customers.add(customer);
        saveCustomers();
    }

    // Function 2: Update customer information
    public void updateCustomer(String code, String name, String phone, String email) throws IllegalArgumentException {
        Customer customer = findCustomerByCode(code);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        if (name != null && !name.trim().isEmpty()) customer.setName(name);
        if (phone != null && !phone.trim().isEmpty()) customer.setPhoneNumber(phone);
        if (email != null && !email.trim().isEmpty()) customer.setEmail(email);
        
        saveCustomers();
    }

    // Function 3: Search for customer by name
    public List<Customer> searchCustomersByName(String searchName) {
        if (searchName == null || searchName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Customer> result = new ArrayList<>();
        String searchLower = searchName.toLowerCase();
        for (Customer c : customers) {
            if (c.getName().toLowerCase().contains(searchLower)) {
                result.add(c);
            }
        }
        Collections.sort(result, new Comparator<Customer>() {
            @Override
            public int compare(Customer c1, Customer c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
        return result;
    }

    // Function 4: Display feast menus
    public List<Menu> getMenus() {
        List<Menu> sortedMenus = new ArrayList<>(menus);
        Collections.sort(sortedMenus, new Comparator<Menu>() {
            @Override
            public int compare(Menu m1, Menu m2) {
                return Double.compare(m1.getPrice(), m2.getPrice());
            }
        });
        return sortedMenus;
    }

    // Function 5: Place a feast order
    public Order placeOrder(String customerCode, String menuCode, int numberOfTables, String eventDateStr) 
            throws IllegalArgumentException {
        Customer customer = findCustomerByCode(customerCode);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        Menu menu = findMenuByCode(menuCode);
        if (menu == null) {
            throw new IllegalArgumentException("Menu not found");
        }

        LocalDate eventDate;
        try {
            eventDate = LocalDate.parse(eventDateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use dd/MM/yyyy");
        }

        if (isDuplicateOrder(customerCode, menuCode, eventDate)) {
            throw new IllegalArgumentException("Duplicate order found for this customer, menu and date");
        }

        Order order = new Order(customerCode, menuCode, numberOfTables, eventDate, menu.getPrice());
        orders.add(order);
        saveOrders();
        return order;
    }

    // Function 6: Update order information
    public void updateOrder(int orderId, String menuCode, Integer numberOfTables, String eventDateStr) 
            throws IllegalArgumentException {
        Order order = findOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        if (order.getEventDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot update past orders");
        }

        if (menuCode != null && !menuCode.trim().isEmpty()) {
            Menu menu = findMenuByCode(menuCode);
            if (menu == null) {
                throw new IllegalArgumentException("Menu not found");
            }
            order.setMenuCode(menuCode);
            order.setPrice(menu.getPrice());
        }

        if (numberOfTables != null) {
            order.setNumberOfTables(numberOfTables);
        }

        if (eventDateStr != null && !eventDateStr.trim().isEmpty()) {
            try {
                LocalDate eventDate = LocalDate.parse(eventDateStr, dateFormatter);
                order.setEventDate(eventDate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use dd/MM/yyyy");
            }
        }

        saveOrders();
    }

    // Function 8: Display lists
    public List<Customer> getAllCustomers() {
        List<Customer> sortedCustomers = new ArrayList<>(customers);
        Collections.sort(sortedCustomers, new Comparator<Customer>() {
            @Override
            public int compare(Customer c1, Customer c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
        return sortedCustomers;
    }

    public List<Order> getAllOrders() {
        List<Order> sortedOrders = new ArrayList<>(orders);
        Collections.sort(sortedOrders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getEventDate().compareTo(o2.getEventDate());
            }
        });
        return sortedOrders;
    }

    // Helper methods
    private Customer findCustomerByCode(String code) {
        for (Customer c : customers) {
            if (c.getCustomerCode().equals(code)) {
                return c;
            }
        }
        return null;
    }

    private Menu findMenuByCode(String code) {
        for (Menu m : menus) {
            if (m.getMenuCode().equals(code)) {
                return m;
            }
        }
        return null;
    }

    private Order findOrderById(int id) {
        for (Order o : orders) {
            if (o.getOrderId() == id) {
                return o;
            }
        }
        return null;
    }

    private boolean isDuplicateOrder(String customerCode, String menuCode, LocalDate eventDate) {
        for (Order o : orders) {
            if (o.getCustomerCode().equals(customerCode) 
                && o.getMenuCode().equals(menuCode) 
                && o.getEventDate().equals(eventDate)) {
                return true;
            }
        }
        return false;
    }

    private void saveCustomers() {
        try {
            FileHandler.saveCustomers(customers);
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    private void saveOrders() {
        try {
            FileHandler.saveOrders(orders);
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }

    public Customer getCustomerByCode(String code) {
        for (Customer customer : customers) {
            if (customer.getCustomerCode().equals(code)) {
                return customer;
            }
        }
        throw new IllegalArgumentException("Customer not found with code: " + code);
    }

    public Menu getMenuByCode(String code) {
        for (Menu menu : menus) {
            if (menu.getMenuCode().equals(code)) {
                return menu;
            }
        }
        throw new IllegalArgumentException("Menu not found with code: " + code);
    }
}