package refactoring;

import refactoring.impl.PaymentProcessor;

public class VnPayPaymentProcessor implements PaymentProcessor {


    @Override
    public void process(double amount) {


        System.out.println(
                "Connecting VNPay API payment: "
                        + amount
        );

    }

}