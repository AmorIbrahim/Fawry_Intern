import java.util.List;
import java.util.stream.Collectors;

public class Customer {
    private String name;
    private double balance;
    private ShoppingCart cart;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.cart = new ShoppingCart();
    }


    public void addToCart(Product product, int quantity) {
        cart.addItem(product, quantity);
    }

    public void checkout() {
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout with empty cart");
        }

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product instanceof Expirable && ((Expirable) product).isExpired()) {
                throw new IllegalStateException("Product " + product.getName() + " is expired");
            }
            if (product.getQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Product " + product.getName() + " is out of stock");
            }
        }

        double subtotal = cart.calculateSubtotal();
        double shippingFee = calculateShippingFee();
        double total = subtotal + shippingFee;

        if (balance < total) {
            throw new IllegalStateException("Insufficient balance");
        }
        balance -= total;

        for (CartItem item : cart.getItems()) {
            item.getProduct().reduceQuantity(item.getQuantity());
        }

        printShipmentNotice();

        printReceipt(subtotal, shippingFee, total);

        cart.clear();
    }

    private double calculateShippingFee() {
        double totalWeight = cart.getItems().stream()
                .filter(item -> item.getProduct() instanceof ShippingService)
                .mapToDouble(item -> ((ShippingService) item.getProduct()).getWeight() * item.getQuantity())
                .sum();

        return totalWeight * 30;
    }

    private void printShipmentNotice() {
        List<CartItem> shippableItems = cart.getItems().stream()
                .filter(item -> item.getProduct() instanceof ShippingService)
                .collect(Collectors.toList());

        if (!shippableItems.isEmpty()) {
            System.out.println("** Shipment notice **");

            double totalWeight = 0;
            for (CartItem item : shippableItems) {
                ShippingService product = (ShippingService) item.getProduct();
                double itemWeight = product.getWeight() * item.getQuantity();
                System.out.printf("%dx %-15s %.0fg%n",
                        item.getQuantity(),
                        product.getName(),
                        itemWeight * 1000);
                totalWeight += itemWeight;
            }

            System.out.printf("Total package weight %.1fkg%n%n", totalWeight);
        }
    }

    private void printReceipt(double subtotal, double shippingFee, double total) {
        System.out.println("** Checkout receipt **");

        for (CartItem item : cart.getItems()) {
            System.out.printf("%dx %-15s %.0f%n",
                    item.getQuantity(),
                    item.getProduct().getName(),
                    item.getProduct().getPrice() * item.getQuantity());
        }

        System.out.println("----------------------");
        System.out.printf("Subtotal         %.0f%n", subtotal);
        System.out.printf("Shipping         %.0f%n", shippingFee);
        System.out.printf("Amount           %.0f%n", total);
        System.out.printf("current balance  %.0f%n", this.balance);
    }
}