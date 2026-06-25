package refactoring.impl;

public interface NotificationService {
    void sendOrderConfirmation(
            User user,
            Order order
    );


}