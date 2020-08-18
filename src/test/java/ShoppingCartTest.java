import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class ShoppingCartTest extends BaseTest {

    private Category food;
    private Category electronic;
    private Product apple;
    private Product milk;
    private Product honey;
    private Product phone;
    private ShoppingCart cart;
    private Campaign campaign1;
    private Campaign campaign5;
    private Coupon coupon1;
    private Coupon coupon2;
    private Coupon coupon3;
    private Coupon coupon4;
    private Coupon coupon5;

    @Before
    public void setUp() {
        food = new Category("food");
        electronic = new Category("electronic");
        apple = new Product("apple", 5, food);
        milk = new Product("milk", 10, food);
        honey = new Product("honey", 20, food);
        phone = new Product("Phone", 100, electronic);
        cart = new ShoppingCart();
        cart.addItem(apple, 4);
        cart.addItem(milk, 2);
        cart.addItem(honey, 1);
        cart.addItem(phone, 1);
        campaign1 = new Campaign(food, 10, 1, new RateDiscountStrategy());
        campaign5 = new Campaign(electronic, 10, 10, new AmountDiscountStrategy());
        coupon1 = new Coupon(10, 10, new RateDiscountStrategy());
        coupon2 = new Coupon(20, 10, new RateDiscountStrategy());
        coupon3 = new Coupon(10, 10, new AmountDiscountStrategy());
        coupon4 = new Coupon(20, 10, new AmountDiscountStrategy());
        coupon5 = new Coupon(1000, 10, new RateDiscountStrategy());
    }


    @Test
    public void getNumberOfDeliveriesShouldReturnDistinctCategoryCount() {
        assertEquals(2, cart.getNumberOfDeliveries());
    }

    @Test
    public void getNumberOfProductsShouldReturnDistinctProductCount() {
        assertEquals(4, cart.getNumberOfProducts());
    }

    @Test
    public void getNumberOfProductsInShouldReturnTotalProductCountInGivenCategory() {
        assertEquals(1, cart.getNumberOfProductsIn(electronic));
        assertEquals(7, cart.getNumberOfProductsIn(food));
    }

    @Test
    public void addingExistingProductToCartShouldIncreaseProductAmount() {
        assertEquals(1, cart.getNumberOfProductsIn(electronic));
        assertEquals(7, cart.getNumberOfProductsIn(food));

        cart.addItem(phone, 1);
        assertEquals(2, cart.getNumberOfProductsIn(electronic));

    }


    @Test
    public void totalPriceInShouldReturnTotalPriceInGivenCategory() {
        assertEquals(100, cart.totalAmountIn(electronic), DELTA);
        assertEquals(5 * 4 + 2 * 10 + 20, cart.totalAmountIn(food), DELTA);
    }

    @Test
    public void getTotalAmountAfterDiscountsShouldApplyOneCoupon() {
        cart.applyDiscounts(coupon1);

        assertEquals(160 - 16, cart.getTotalAmountAfterDiscounts(), DELTA);
    }

    @Test
    public void getTotalAmountAfterDiscountsShouldNotApplyTheCouponThatDoesNotMeetMinAmount() {
        cart.applyDiscounts(coupon5);

        assertEquals(160, cart.getTotalAmountAfterDiscounts(), DELTA);
    }

    @Test
    public void getTotalAmountAfterDiscountsShouldApplyTwoCouponsWithRate() {
        cart.applyDiscounts(coupon1, coupon2);

        assertEquals(160 - 32, cart.getTotalAmountAfterDiscounts(), DELTA);
    }

    @Test
    public void getTotalAmountAfterDiscountsShouldApplyTwoCouponsWithAmount() {
        cart.applyDiscounts(coupon3, coupon4);

        assertEquals(160 - 20, cart.getTotalAmountAfterDiscounts(), DELTA);
    }

    @Test
    public void getTotalAmountAfterDiscountsShouldApplyTwoCouponsWithAmountAndRate() {
        cart.applyDiscounts(coupon3, coupon1);

        assertEquals(160 - 16 - 10, cart.getTotalAmountAfterDiscounts(), DELTA);
    }

    @Test
    public void getTotalAmountAfterDiscountsShouldApplyTwoCouponsWithRateAndAmount() {
        cart.applyDiscounts(coupon1, coupon3);

        assertEquals(160 - 16 - 10, cart.getTotalAmountAfterDiscounts(), DELTA);
    }

    @Test
    public void getTotalAmountAfterDiscountsShouldApplyOneCampaign() {
        cart.applyDiscounts(campaign1);

        assertEquals(160 - 6, cart.getTotalAmountAfterDiscounts(), DELTA);
    }

    @Test
    public void getTotalAmountAfterDiscountsShouldNotApplyTheCampaignThatDoesNotRequiredProductCount() {
        cart.applyDiscounts(campaign5);

        assertEquals(160, cart.getTotalAmountAfterDiscounts(), DELTA);
    }


    @Test
    public void getCouponDiscountsShouldReturnOnlyCouponDiscounts() {
        cart.applyDiscounts(coupon1, campaign1);
        Set<Coupon> coupons = cart.getCouponDiscounts();

        assertEquals(1, coupons.size());
    }


    @Test
    public void getCampaignDiscountsShouldReturnOnlyCampaignDiscounts() {
        cart.applyDiscounts(coupon1, campaign1);
        Set<Campaign> campaigns = cart.getCampaignDiscounts();

        assertEquals(1, campaigns.size());
    }

    @Test
    public void oneCampaignShouldBeAppliedOnlyOnce() {
        cart.applyDiscounts(campaign1);
        cart.applyDiscounts(campaign1);
        Set<Campaign> campaigns = cart.getCampaignDiscounts();

        assertEquals(1, campaigns.size());
    }

    @Test
    public void oneCouponShouldBeAppliedOnlyOnce() {
        cart.applyDiscounts(coupon1);
        cart.applyDiscounts(coupon1);
        Set<Coupon> coupons = cart.getCouponDiscounts();

        assertEquals(1, coupons.size());
    }

    @Test
    public void print() {
        cart.applyDiscounts(coupon1, campaign1);
        String printedText = cart.print();
        assertThat(printedText, containsString("apple 4 5.0 Total price: 20.0 Total discount: 2.0"));
        assertThat(printedText, containsString("milk 2 10.0 Total price: 20.0 Total discount: 2.0"));
        assertThat(printedText, containsString("honey 1 20.0 Total price: 20.0 Total discount: 2.0"));
        assertThat(printedText, containsString("food"));
        assertThat(printedText, containsString("electronic"));
        assertThat(printedText, containsString("Phone 1 100.0 Total price: 100.0 Total discount: 0.0"));
        assertThat(printedText, containsString("Coupon applied: 16.0"));
        assertThat(printedText, containsString("Total: 138.0"));
        assertThat(printedText, containsString("Delivery costs: 6.99"));
    }

}
