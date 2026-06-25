package refactoring;

import refactoring.impl.VoucherStrategy;

public class VipDiscountStrategy implements VoucherStrategy {


    @Override
    public double apply(double total) {

        return total * 0.8;

    }

}
