// view/MainMenu.java
package View;

import Controller.MenuController;
import Model.Customer;
import Model.Menu;
import Model.Order;
import Utils.Utils;

import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class MenuView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final MenuController controller = new MenuController();

    public static void displayMenu() {
        System.out.println("\n=== Traditional Feast Order Management System ===");
        System.out.println("1. Register customers");
        System.out.println("2. Update customer information");
        System.out.println("3. Search for customer by name");
        System.out.println("4. Display feast menus");
        System.out.println("5. Place a feast order");
        System.out.println("6. Update order information");
        System.out.println("7. Save data to file");
        System.out.println("8. Display Customer/Order lists");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    public static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

   public static void processUserChoice(int choice) {
        if (choice < 0 || choice > 8) {
            System.out.println("\nInvalid choice! Please select a number between 0 and 8.");
            return;
        }

        if (choice == 0) {
            System.out.println("\nThank you for using our program! Goodbye!");
            System.exit(0);
        }

        String featureName;
        switch (choice) {
            case 1:
                featureName = "Register New Customer";
                break;
            case 2:
                featureName = "Update Customer Information";
                break;
            case 3:
                featureName = "Search Customer by Name";
                break;
            case 4:
                featureName = "Display Feast Menus";
                break;
            case 5:
                featureName = "Place Feast Order";
                break;
            case 6:
                featureName = "Update Order Information";
                break;
            case 7:
                featureName = "Save Data to File";
                break;
            case 8:
                featureName = "Display Lists";
                break;
            default:
                featureName = "";
                break;
        }

        System.out.println("\n=== " + featureName + " ===");

        try {
            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    updateCustomer();
                    break;
                case 3:
                    searchCustomer();
                    break;
                case 4:
                    displayMenus();
                    break;
                case 5:
                    placeOrder();
                    break;
                case 6:
                    updateOrder();
                    break;
                case 7:
                    saveData();
                    break;
                case 8:
                    displayLists();
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace(); // For debugging
        }

        System.out.println("\nPress Enter to return to main menu...");
        scanner.nextLine();
    }

    private static void registerCustomer() {
        String code;
        while (true) {
            code = Utils.getString(
                "Enter customer code (C/G/K followed by 4 digits): ",
                "Invalid code format. Must start with C, G, or K followed by 4 digits",
                "^[CGK]\\d{4}$"
            );
            
            // Check if customer code exists immediately
            try {
                List<Customer> customers = controller.getAllCustomers();
                boolean exists = false;
                for (Customer c : customers) {
                    if (c.getCustomerCode().equals(code)) {
                        exists = true;
                        break;
                    }
                }
                if (exists) {
                    System.out.println("Error: Customer code already exists");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }
        }
        
        String name = Utils.getString(
            "Enter customer name (2-25 characters): ",
            "Name must be between 2 and 25 characters",
            "^[\\p{L}\\s]{2,25}$"
        );
        
        String phone = Utils.getString(
            "Enter phone number (10 digits): ",
            "Invalid phone number format",
            "^0\\d{9}$"
        );
        
        String email = Utils.getString(
            "Enter email: ",
            "Invalid email format",
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        );

        controller.registerCustomer(code, name, phone, email);
        System.out.println("Customer registered successfully!");
    }

    private static void updateCustomer() {
        do {
            String code = Utils.getString(
                "Enter customer code (C/G/K followed by 4 digits): ",
                "Invalid code format. Must start with C, G, or K followed by 4 digits",
                "^[CGK]\\d{4}$"
            );

            try {
                // Check if customer exists before proceeding with update
                List<Customer> customers = controller.getAllCustomers();
                boolean customerExists = false;
                for (Customer c : customers) {
                    if (c.getCustomerCode().equals(code)) {
                        customerExists = true;
                        break;
                    }
                }

                if (!customerExists) {
                    System.out.println("This customer does not exist.");
                } else {
                    System.out.println("\nLeave blank to keep current value");
                    
                    // Get new name with validation
                    String name = Utils.getString(
                        "Enter new name (2-25 characters) or press Enter to skip: ",
                        "Name must be between 2 and 25 characters",
                        "^$|^[\\p{L}\\s]{2,25}$"
                    );

                    // Get new phone with validation
                    String phone = Utils.getString(
                        "Enter new phone number (10 digits) or press Enter to skip: ",
                        "Invalid phone number format",
                        "^$|^0\\d{9}$"
                    );

                    // Get new email with validation
                    String email = Utils.getString(
                        "Enter new email or press Enter to skip: ",
                        "Invalid email format",
                        "^$|^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
                    );

                    controller.updateCustomer(code, 
                        name.isEmpty() ? null : name,
                        phone.isEmpty() ? null : phone,
                        email.isEmpty() ? null : email);
                    System.out.println("Customer updated successfully!");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.print("\nDo you want to update another customer? (Y/N): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("y") && !answer.equals("yes")) {
                break;
            }
        } while (true);
    }

    private static void searchCustomer() {
        System.out.print("Enter search name: ");
        String searchName = scanner.nextLine().trim();

        List<Customer> results = controller.searchCustomersByName(searchName);
        if (results.isEmpty()) {
            System.out.println("No one matches the search criteria!");
        } else {
            System.out.println("\nMatching Customers: " + searchName);
            String[] headers = {"Code", "Customer Name", "Phone", "Email"};
            int[] columnWidths = {6, 25, 12, 25};

            Utils.drawTableHeader(headers, columnWidths);
            for (Customer customer : results) {
                String[] rowData = {
                    customer.getCustomerCode(),
                    customer.getName(),
                    customer.getPhoneNumber(),
                    customer.getEmail()
                };
                Utils.drawTableRow(rowData, columnWidths);
            }
            Utils.drawTableFooter(columnWidths);
        }
    }

    private static void displayMenus() {
        System.out.println("\nList of Set Menus for ordering party:");
        System.out.println("------------------------------------------------");
        List<Menu> menus = controller.getMenus();
        if (menus.isEmpty()) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            return;
        }

        for (Menu menu : menus) {
            System.out.println("Code        :" + menu.getMenuCode());
            System.out.println("Name        :" + menu.getName());
            System.out.printf("Price       : %,d Vnd%n", (int)menu.getPrice());
            System.out.println("Ingredients:");
            
            List<String> ingredients = menu.getIngredients();
            for (String ingredient : ingredients) {
                System.out.println("+ " + ingredient);
            }
            
            // Add separator between menus
            System.out.println("------------------------------------------------");
        }
    }

    private static void placeOrder() {
        System.out.println("\nCase 1: Order successful");
        System.out.println("------------------------------------------------");
        
        String customerCode = Utils.getString(
            "Enter customer code: ",
            "Invalid customer code format",
            "^[CGK]\\d{4}$"
        );
        
        String menuCode = Utils.getString(
            "Enter menu code: ",
            "Invalid menu code format",
            "^PW\\d{3}$"
        );
        
        int tables = Utils.getInt(
            "Enter number of tables: ",
            "Invalid number of tables",
            1, 100
        );
        
        String date = Utils.getString(
            "Enter event date (dd/MM/yyyy): ",
            "Invalid date format",
            "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$"
        );

        Order order = controller.placeOrder(customerCode, menuCode, tables, date);
        
        // Display order confirmation in the specified format
        System.out.println("\nCustomer order information [Order ID: " + order.getOrderId() + "]");
        System.out.println("------------------------------------------------");
        System.out.println("Code           : " + order.getCustomerCode());
        
        // Get customer details
        Customer customer = controller.getCustomerByCode(order.getCustomerCode());
        System.out.println("Customer name  : " + customer.getName());
        System.out.println("Phone number   : " + customer.getPhoneNumber());
        System.out.println("Email         : " + customer.getEmail());
        System.out.println("------------------------------------------------");
        
        // Get menu details
        Menu menu = controller.getMenuByCode(order.getMenuCode());
        System.out.println("Code of Set Menu: " + order.getMenuCode());
        System.out.println("Set menu name   : " + menu.getName());
        System.out.println("Event date      : " + order.getEventDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Number of tables: " + order.getNumberOfTables());
        System.out.printf("Price           : %,d Vnd%n", (int)order.getPrice());
        System.out.println("Ingredients:");
        for (String ingredient : menu.getIngredients()) {
            System.out.println("+ " + ingredient);
        }
        System.out.println("------------------------------------------------");
        System.out.printf("Total cost      : %,d Vnd%n", (int)order.getTotalCost());
        System.out.println("------------------------------------------------");
    }

    private static void updateOrder() {
        System.out.print("Enter order ID: ");
        int orderId = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Leave blank to keep current value");
        System.out.print("Enter new menu code (or press Enter to skip): ");
        String menuCode = scanner.nextLine().trim();
        System.out.print("Enter new number of tables (or press Enter to skip): ");
        String tablesStr = scanner.nextLine().trim();
        System.out.print("Enter new event date (dd/MM/yyyy) (or press Enter to skip): ");
        String date = scanner.nextLine().trim();

        controller.updateOrder(orderId,
            menuCode.isEmpty() ? null : menuCode,
            tablesStr.isEmpty() ? null : Integer.parseInt(tablesStr),
            date.isEmpty() ? null : date);
        System.out.println("Order updated successfully!");
    }

    private static void saveData() {
        // Data is automatically saved after each operation
        System.out.println("All data has been saved successfully!");
    }

    private static void displayLists() {
        System.out.println("1. Display Customer List");
        System.out.println("2. Display Order List");
        
        int choice = Utils.getInt(
            "Enter your choice: ",
            "Invalid choice",
            1, 2
        );
        
        if (choice == 1) {
            displayCustomerList();
        } else {
            displayOrderList();
        }
    }

    private static void displayCustomerList() {
        List<Customer> customers = controller.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        System.out.println("\nCustomers information:");
        System.out.println("------------------------------------------------------------");
        System.out.println("Code     | Customer Name        | Phone        | Email");
        System.out.println("------------------------------------------------------------");
        for (Customer customer : customers) {
            System.out.printf("%-8s | %-18s | %-11s | %s%n",
                customer.getCustomerCode(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getEmail());
        }
        System.out.println("------------------------------------------------------------");
    }

    private static void displayOrderList() {
        List<Order> orders = controller.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("\n------------------------------------------------------------");
        System.out.println("ID     | Event date  |Customer ID| Set Menu| Price     | Tables |     Cost");
        System.out.println("------------------------------------------------------------");
        for (Order order : orders) {
            System.out.printf("%-6s | %-10s | %-9s | %-7s | %-9d| %6d | %,9d%n",
                order.getOrderId(),
                order.getEventDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                order.getCustomerCode(),
                order.getMenuCode(),
                (int)order.getPrice(),
                order.getNumberOfTables(),
                (int)order.getTotalCost());
        }
        System.out.println("------------------------------------------------------------");
    }
}