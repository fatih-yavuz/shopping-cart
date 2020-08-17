abstract class Discount implements Comparable {

    protected double discountQuantity;
    protected DiscountStrategy discountStrategy;

    public Discount(double discountQuantity, DiscountStrategy discountStrategy) {
        this.discountQuantity = discountQuantity;
        this.discountStrategy = discountStrategy;
    }

    abstract double calculateDiscount(ShoppingCart cart);

    @Override
    public int compareTo(Object o) {
        Discount discount = (Discount) o;
        DiscountStrategyComparator comparator = new DiscountStrategyComparator();
        if (comparator.compare(this.discountStrategy, discount.discountStrategy) > 0) {
            return 1;
        }
        if (comparator.compare(this.discountStrategy, discount.discountStrategy) < 0) {
            return -1;
        }
        return Double.compare(this.discountQuantity, discount.discountQuantity);
    }
}
