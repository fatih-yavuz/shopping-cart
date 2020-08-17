public class RateDiscountStrategy implements DiscountStrategy {
    @Override
    public double discount(double totalPriceIn, double discountQuantity) {
        return totalPriceIn * discountQuantity / 100;
    }
}
