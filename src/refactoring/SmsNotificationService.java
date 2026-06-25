package refactoring;

import refactoring.impl.NotificationService;

public class SmsNotificationService implements NotificationService {


    @Override
    public void sendOrderConfirmation(
            User user,
            Order order
    ){

        System.out.println(
                "Sending SMS notification"
        );

    }

}
