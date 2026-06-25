package refactoring;

import refactoring.impl.PaymentProcessor;

public class MomoPaymentProcessor implements PaymentProcessor {
    @Override
    public void process(double amount) {

        System.out.println(
                "Connecting Momo API payment: "
                        + amount
        );

    }

}