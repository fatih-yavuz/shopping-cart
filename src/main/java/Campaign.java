public class Campaign extends Discount {
    private Category category;
    private int requiredProductQuantity;

    public Campaign(Category category, double discountQuantity, int requiredProductQuantity,
                    DiscountStrategy discountStrategy) {
        super(discountQuantity, discountStrategy);
        this.category = category;
        this.requiredProductQuantity = requiredProductQuantity;
    }

    @Override
    public double calculateDiscount(ShoppingCart cart) {
        if (cart.getNumberOfProductsIn(category) < requiredProductQuantity) {
            return 0;
        }
        return discountStrategy.discount(cart.totalAmountIn(category), discountQuantity);
    }

    public Category getCategory() {
        return category;
    }

    public double calculateDiscount(Product product, int quantity) {
        return discountStrategy.discount(product.getPrice() * quantity, discountQuantity);
    }
}
