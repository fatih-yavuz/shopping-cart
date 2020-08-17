import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCart {
    private Map<Product, Integer> productsAndQuantities = new HashMap<>();
    private Set<Coupon> couponDiscounts = new HashSet<>();
    private Set<Campaign> campaignDiscounts = new HashSet<>();
    private double totalDiscountAmount;

    public void addItem(Product product, int quantity) {
        int prevQuantity = productsAndQuantities.getOrDefault(product, 0);
        productsAndQuantities.put(product, quantity + prevQuantity);
    }

    public void applyDiscounts(Discount... discounts) {

        List<Campaign> campaigns =
                Arrays.stream(discounts).filter(discount -> discount instanceof Campaign).map(discount -> (Campaign) discount).collect(Collectors.toList());
        List<Coupon> coupons =
                Arrays.stream(discounts).filter(discount -> discount instanceof Coupon).map(discount -> (Coupon) discount).collect(Collectors.toList());

        campaigns.sort(Collections.reverseOrder());
        coupons.sort(Collections.reverseOrder());

        for (Campaign campaign : campaigns) {
            applyCampaign(campaign);
        }

        for (Coupon coupon : coupons) {
            applyCoupon(coupon);
        }
    }

    private boolean applyDiscount(Discount discount) {

        double discountAmount = discount.calculateDiscount(this);
        if (discountAmount > 0) {
            totalDiscountAmount += discountAmount;
            return true;
        }
        return false;

    }

    private void applyCampaign(Campaign campaign) {
        if (!campaignDiscounts.contains(campaign) && applyDiscount(campaign)) {
            campaignDiscounts.add(campaign);
        }
    }

    private void applyCoupon(Coupon coupon) {
        if (!couponDiscounts.contains(coupon) && applyDiscount(coupon)) {
            couponDiscounts.add(coupon);
        }
    }


    public double getTotalAmountAfterDiscounts() {
        return getTotalAmount() - totalDiscountAmount;
    }

    public Set<Coupon> getCouponDiscounts() {
        return couponDiscounts;
    }

    public Set<Campaign> getCampaignDiscounts() {
        return campaignDiscounts;
    }

    //    public String print() {
    //        return "";
    //    }

    public int getNumberOfDeliveries() {
        return productsAndQuantities
                .keySet().stream().map(Product::getCategory).collect(Collectors.toSet()).size();
    }

    public int getNumberOfProducts() {
        return productsAndQuantities.keySet().size();
    }

    public double getDeliveryCosts(DeliveryCostCalculator calculator) {
        return calculator.calculateFor(this);
    }

    public int getNumberOfProductsIn(Category category) {
        return productsAndQuantities
                .keySet().stream().filter(product -> product.getCategory() == category)
                .mapToInt(product -> productsAndQuantities.get(product)).sum();
    }

    public double totalAmountIn(Category category) {
        return productsAndQuantities
                .keySet().stream().filter(product -> product.getCategory() == category)
                .mapToDouble(product -> product.getPrice() * productsAndQuantities.get(product))
                .sum();
    }

    public double getTotalAmount() {
        return productsAndQuantities
                .keySet().stream()
                .mapToDouble(product -> product.getPrice() * productsAndQuantities.get(product))
                .sum();
    }
}
