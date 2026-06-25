package refactoring;


import refactoring.impl.NotificationService;
import refactoring.impl.PaymentProcessor;
import refactoring.impl.VoucherStrategy;

public class OrderService {

    private final PaymentProcessor paymentProcessor;
    private final NotificationService notificationService;
    private final VoucherStrategy voucherStrategy;


    public OrderService(PaymentProcessor paymentProcessor, NotificationService notificationService, VoucherStrategy voucherStrategy){
        this.paymentProcessor = paymentProcessor;
        this.notificationService = notificationService;
        this.voucherStrategy = voucherStrategy;

    }

    public Order checkout(Cart cart, User user){
        if(user.getStatus()!=1){
            throw new RuntimeException("User locked");
        }
        double total = calculateTotal(cart);
        total = voucherStrategy.apply(total);
        paymentProcessor.process(total);
        Order order = new Order(user, total, "SUCCESS");

        notificationService.sendOrderConfirmation(user, order);
        return order;

    }
    private double calculateTotal(Cart cart){
        double total = 0;
        for(Item i : cart.getItems()){
            total += i.getPrice() * i.getQuantity();

        }
        return total;
    }

}



