package refactoring;

import refactoring.impl.VoucherStrategy;

public class FreeShipStrategy implements VoucherStrategy {


    @Override
    public double apply(double total) {

        return total - 30000;

    }

}