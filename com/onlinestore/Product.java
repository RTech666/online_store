package online_store.com.onlinestore;

public class Product {
    // Create the variables, as private.
    private String sku;
    private String name;
    private double price;
    private String department;

    // Create the constrcutor.
    public Product(String sku, String name, double price, String department) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.department = department;
    }

    // Create the getters.
    public String getSKU() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDepartment() {
        return department;
    }
}
