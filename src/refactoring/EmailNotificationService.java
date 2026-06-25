package refactoring;

import refactoring.impl.NotificationService;

public class EmailNotificationService implements NotificationService {

    @Override
    public void sendOrderConfirmation(
            User user,
            Order order
    ){

        System.out.println(
                "Sending email to "
                        + user.getEmail()
        );

    }

}
