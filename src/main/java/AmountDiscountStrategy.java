public class AmountDiscountStrategy implements DiscountStrategy {
    @Override
    public double discount(double totalAmount, double discountQuantity) {
        return discountQuantity;
    }
}
