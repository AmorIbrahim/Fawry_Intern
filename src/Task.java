import java.time.LocalDate;

public class Task {
    public static void main(String [] args){

        Product tv = new ShippingServiceProduct("Smart TV", 1000, 10, 15.5);
        Product mobile = new NonExpireProduct("Smartphone", 850, 20);
        Product cheese = new ShippingServiceProductExpire("Cheese", 100, 50, LocalDate.now().plusDays(7),.2);
        Product scratchCard = new NonExpireProduct("Mobile Scratch Card", 10.00, 100);
        Product biscuits = new ShippingServiceProductExpire("Biscuits", 150, 5,LocalDate.now().plusDays(7),.7);

        Customer customer = new Customer("John ", 10000.00);

        try {
           customer.addToCart(tv, 3);
           customer.addToCart(mobile, 1);
           customer.addToCart(cheese, 2);
           customer.addToCart(scratchCard, 1);
           customer.addToCart(biscuits, 1);

           customer.checkout();
        } catch (Exception e) {
            System.out.println("Error during checkout: " + e.getMessage());
        }
    }
}
