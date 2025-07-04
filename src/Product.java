import java.time.LocalDate;

public abstract class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void reduceQuantity(int amount) {
        if (amount > quantity) {
            throw new IllegalArgumentException("Cannot reduce more than available quantity");
        }
        quantity -= amount;
    }
}
interface Expirable {
    boolean isExpired();
}

interface ShippingService {
    String getName();
    double getWeight();
}
class NonExpireProduct extends Product {
    public NonExpireProduct(String name, double price, int quantity) {
        super(name, price, quantity);
    }
}

class ExpireProduct extends Product implements Expirable {
    private LocalDate expiryDate;

    public ExpireProduct(String name, double price, int quantity, LocalDate expiryDate) {
        super(name, price, quantity);
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
}

class ShippingServiceProduct extends Product implements ShippingService {
    private double weight;

    public ShippingServiceProduct(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}

class ShippingServiceProductExpire extends ExpireProduct implements ShippingService {
    private double weight;

    public ShippingServiceProductExpire(String name, double price, int quantity, LocalDate expiryDate, double weight) {
        super(name, price, quantity,expiryDate);
        this.weight=weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}


