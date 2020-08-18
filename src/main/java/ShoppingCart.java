import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCart {
    private Map<Product, Integer> productsAndQuantities = new HashMap<>();
    private Map<Category, List<Product>> categoriesAndProducts = new HashMap<>();
    private Map<Category, List<Campaign>> categoriesAndCampaigns = new HashMap<>();
    private Set<Coupon> couponDiscounts = new HashSet<>();
    private Set<Campaign> campaignDiscounts = new HashSet<>();
    private double totalDiscountAmount;
    private DeliveryCostCalculator deliveryCostCalculator = DeliveryCostCalculatorFactory.createCalculator();

    public void addItem(Product product, int quantity) {
        int prevQuantity = productsAndQuantities.getOrDefault(product, 0);
        productsAndQuantities.put(product, quantity + prevQuantity);
        List<Product> products = categoriesAndProducts.getOrDefault(product.getCategory(), new ArrayList<>());
        products.add(product);
        categoriesAndProducts.put(product.getCategory(), products);
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
            List<Campaign> campaigns = categoriesAndCampaigns.getOrDefault(campaign.getCategory(), new ArrayList<>());
            campaigns.add(campaign);
            categoriesAndCampaigns.put(campaign.getCategory(), campaigns);
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

    private double discountOf(Product product) {
        double discountPrice = 0;
        Category category = product.getCategory();
        List<Campaign> campaigns = categoriesAndCampaigns.get(category);
        if (campaigns != null) {
            campaigns.sort(Collections.reverseOrder());
            for (Campaign campaign : campaigns) {
                discountPrice += campaign.calculateDiscount(product, productsAndQuantities.get(product));
            }
        }
        return discountPrice;
    }

    public String print() {
        StringBuilder builder = new StringBuilder();
        for (Category category : categoriesAndProducts.keySet()) {
            builder.append(category).append("\n");
            for (Product product : categoriesAndProducts.get(category)) {
                int quantity = productsAndQuantities.get(product);
                builder.append("\t").append(product).append(" ");
                builder.append(quantity).append(" ");
                builder.append(product.getPrice()).append(" ");
                builder.append("Total price: ").append(product.getPrice() * quantity).append(" ");
                builder.append("Total discount: ").append(discountOf(product));
                builder.append("\n");
            }
        }
        for (Coupon coupon : couponDiscounts) {
            builder.append("Coupon applied: ").append(coupon.calculateDiscount(this));
        }
        builder.append("\n");
        builder.append("Total: ").append(getTotalAmountAfterDiscounts()).append("\n");
        builder.append("Delivery costs: ").append(getDeliveryCosts());
        return builder.toString();
    }

    public int getNumberOfDeliveries() {
        return productsAndQuantities
                .keySet().stream().map(Product::getCategory).collect(Collectors.toSet()).size();
    }

    public int getNumberOfProducts() {
        return productsAndQuantities.keySet().size();
    }

    public double getDeliveryCosts() {
        return deliveryCostCalculator.calculateFor(this);
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
