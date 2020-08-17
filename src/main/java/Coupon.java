public class Coupon extends Discount {
    private double minAmount;

    public Coupon(double minAmount, double discountQuantity,
                  DiscountStrategy discountStrategy) {
        super(discountQuantity, discountStrategy);
        this.minAmount = minAmount;
    }

    @Override
    public double calculateDiscount(ShoppingCart cart) {
        if (cart.getTotalAmount() < minAmount) {
            return 0;
        }
        return discountStrategy.discount(cart.getTotalAmount(), discountQuantity);
    }
}
