package online_store;
import online_store.com.onlinestore.Product;
import online_store.com.onlinestore.Cart;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class OnlineStore {
    // Initalize the scanner.
    static Scanner scanner = new Scanner(System.in);

    // Create the list.
    static List<Product> products;

    // Create the variables.
    static Cart cart;
    static OnlineStore store;
    static int choice;
    static String sku;
    static String name;
    static double price;
    static String department;
    static boolean firstLine = true;
    static boolean found;
    static String keyword;
    static Product productToAdd;
    static Product item;
    static String skuToRemove;
    static Product productToRemove;
    static Map<Product, Integer> itemQuantities;
    static int quantity;
    static double amountPaid;
    static double change;
    static SimpleDateFormat dateFormat;
    static String timestamp;
    static String fileName;
    static PrintWriter writer;
    static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        store = new OnlineStore();

        while (true) {
            try {
                // Print the home screen.
                System.out.println("\nThe Store Home Screen");
                System.out.println("1. Display Products");
                System.out.println("2. Display Cart");
                System.out.println("3. Exit");

                // Ask the user what option would they like.
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();

                // Read the user input and execute the appropiate method.
                switch (choice) {
                    case 1:
                        store.displayProducts();
                        break;
                    case 2:
                        store.displayCart();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                        break;
                }
            // Error handling.
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine();
            }
        }
    }

    // Create the OnlineStore method.
    public OnlineStore() {
        // Create the array.
        products = new ArrayList<>();
        cart = new Cart();
        
        // Call the loadProductsFromCSV method to load the products from the CSV.
        loadProductsFromCSV("products.csv");
    }

    // Create the loadProductsFromCSV method.
    public void loadProductsFromCSV(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                // Ignore the first line of the CSV.
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Split the products from products.csv.
                String[] parts = line.split("\\|");

                // Set the values from the parts.
                sku = parts[0];
                name = parts[1];
                price = Double.parseDouble(parts[2]);
                department = parts[3];

                // Add it to the array.
                products.add(new Product(sku, name, price, department));
            }
        // If their is an error, print it.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create the displayProducts method.
    public void displayProducts() {
        while (true) {
            // Print out the available products.
            System.out.println("\nAvailable Products:");
            for (Product product : products) {
                System.out.println("SKU: " + product.getSKU() + " | Product: " + product.getName() + " | Price: $" + product.getPrice() + " | Department: " + product.getDepartment());
            }
            
            // Print out the options.
            System.out.println("\nOptions:");
            System.out.println("1. Search products");
            System.out.println("2. Add a product to cart");
            System.out.println("3. Go Back to the home page");
            
            // Ask the user what they want to do next.
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            
            // Read the user input and execute the appropiate method.
            switch (choice) {
                case 1:
                    searchProducts();
                    break;
                case 2:
                    addProductToCart();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }
    
    // Create searchProducts method.
    public void searchProducts() {
        // Ask the user for the search keyword.
        System.out.print("\nEnter search keyword: ");
        keyword = scanner.nextLine().toLowerCase();
        
        // Print out the search results.
        System.out.println("\nSearch Results:");

        // Set the variable.
        found = false;

        // If product was found, print it.
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(keyword) || 
                product.getDepartment().toLowerCase().contains(keyword)) {
                    System.out.println("SKU: " + product.getSKU() + " | Product: " + product.getName() + " | Price: $" + product.getPrice() + " | Department: " + product.getDepartment());
                found = true;
            }
        }
        // If a product was not found, print message.
        if (!found) {
            System.out.println("Product not found.");
        }
    }
    
    // Create addProductToCart method.
    public void addProductToCart() {
        // Ask user to enter SKU of the item they want to add to cart.
        System.out.print("\nEnter the SKU of the product to add to cart: ");
        sku = scanner.nextLine();
        
        // Create the variable.
        productToAdd = null;

        // Find the product with the entered SKU.
        for (Product product : products) {
            if (product.getSKU().equalsIgnoreCase(sku)) {
                productToAdd = product;
                break;
            }
        }
        
        // Add product to cart.
        if (productToAdd != null) {
            cart.addToCart(productToAdd);
            System.out.println("\nProduct added to cart.");
        // Print message if product not found.
        } else {
            System.out.println("\nProduct not found.");
        }
    }

    // Create the displayCart method.
    public void displayCart() {
        while (true) {
            // Get the items that are in the cart from the list.
            List<Product> cartItems = cart.getItems();
    
            // If their cart is empty, print message.
            if (cartItems.isEmpty()) {
                System.out.println("\nYour cart is empty.");
                break;
            // If they have items in their cart, print what they have with prices and total.
            } else {
                System.out.println("\nYour Cart:");

                // Initalize the Map.
                itemQuantities = new HashMap<>();

                // Check if user added more than 1 of the same product.
                for (Product item : cartItems) {
                    itemQuantities.put(item, itemQuantities.getOrDefault(item, 0) + 1);
                }
                // Print the cart with all the information.
                for (Map.Entry<Product, Integer> entry : itemQuantities.entrySet()) {
                    item = entry.getKey();
                    quantity = entry.getValue();
                    System.out.println("SKU: " + item.getSKU() + " | Product: " + item.getName() + " | Price: $" + item.getPrice() + " | Department: " + item.getDepartment());
                }
                System.out.println("\nTotal: $" + cart.getTotalPrice());
                
                // Print out the options.
                System.out.println("\nOptions:");
                System.out.println("1. Check Out");
                System.out.println("2. Remove Product from the cart");
                System.out.println("3. Go Back to the home screen");
                
                // Ask the user what they want to do next.
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();
                scanner.nextLine();
                
                // Read the user input and execute the appropiate method.
                switch (choice) {
                    case 1:
                        checkout(cartItems);
                        break;
                    case 2:
                        removeProductFromCart(cartItems);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid input.");
                }
            }
        }
    }
    
    // Create the removeProductFromCart method.
    public void removeProductFromCart(List<Product> cartItems) {
        // Ask user to enter the SKU of the product they want to remove.
        System.out.print("Enter the SKU of the product to remove: ");
        skuToRemove = scanner.nextLine();
        
        // Create the variable.
        productToRemove = null;
        
        // Look for the product to remove that matches the SKU entered.
        for (Product item : cartItems) {
            if (item.getSKU().equalsIgnoreCase(skuToRemove)) {
                productToRemove = item;
                break;
            }
        }
        
        // If product was found, remove the item.
        if (productToRemove != null) {
            cartItems.remove(productToRemove);
            System.out.println(productToRemove.getName() + " removed from cart.");
        // If product not found, print message.
        } else {
            System.out.println("Product not found in cart.");
        }
    }

    // Create checkout method.
    public void checkout(List<Product> cartItems) {
        // Print the total.
        System.out.println("\nTotal amount: $" + cart.getTotalPrice());

        // Ask the user how much cash they will pay with.
        while (amountPaid < cart.getTotalPrice()) {
            System.out.print("\nEnter the amount paid: $");
    
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
                continue;
            }
    
            amountPaid = scanner.nextDouble();
    
            if (amountPaid < cart.getTotalPrice()) {
                System.out.println("Insufficient payment. Please provide enough cash to cover the total amount.");
            }
        }
        
        // Calculate the change to give the user and print it.
        change = amountPaid - cart.getTotalPrice();
        System.out.println("\nChange: $" + df.format(change));
        
        // Create the sales receipt file.
        generateSalesReceipt(cartItems, cart.getTotalPrice(), amountPaid, change);

        // Print thank you and exit.
        System.out.println("Thank you for your purchase!");
        System.exit(0);
    }

    // Create generateSalesReceipt method.
    public void generateSalesReceipt(List<Product> cartItems, double totalAmount, double amountPaid, double change) {
        try {
            // Set the date format.
            dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
            timestamp = dateFormat.format(new Date());
            
            // Set where to save the receipt and the filename.
            fileName = timestamp + ".txt";
            
            // Initalize the PrintWriter.
            writer = new PrintWriter(new FileWriter(fileName));

            // What to put in the file.
            writer.println("Order Date: " + new Date());
            writer.println("Items:");

            // Initalize the Map.
            itemQuantities = new HashMap<>();

            // Check if user added more than 1 of the same product.
            for (Product item : cartItems) {
                itemQuantities.put(item, itemQuantities.getOrDefault(item, 0) + 1);
            }
            // Write the checked out products with all the information.
            for (Map.Entry<Product, Integer> entry : itemQuantities.entrySet()) {
                item = entry.getKey();
                quantity = entry.getValue();
                writer.println("- SKU: " + item.getSKU() + " | Product: " + item.getName() + " | Price: $" + item.getPrice() + " | Department: " + item.getDepartment());
            }

            // Write the rest of the information.
            writer.println("Sales Total: $" + totalAmount);
            writer.println("Amount Paid: $" + amountPaid);
            writer.println("Change Given: $" + df.format(change));

            // Close PrintWriter.
            writer.close();
        // If their is an error, print it.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}