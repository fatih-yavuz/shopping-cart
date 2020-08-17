import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeliveryCostCalculatorTest extends BaseTest {

    private Category food;
    private Category car;
    private Product apple;
    private Product audi;
    private ShoppingCart cart;
    private DeliveryCostCalculator deliveryCostCalculator;

    @Before
    public void setUp() {
        deliveryCostCalculator = new DeliveryCostCalculatorImpl(1, 2, 2.99);
        food = new Category("food");
        car = new Category("car");
        apple = new Product("apple", 5, food);
        audi = new Product("audi a8", 15, car);
        cart = new ShoppingCart();
        cart.addItem(apple, 1);
    }

    @Test
    public void calculateForShouldCalculateCorrectCostForOneProduct() {
        double deliveryCost = deliveryCostCalculator.calculateFor(cart);

        assertEquals(2.99 + 1 + 2, deliveryCost, DELTA);
    }

    @Test
    public void calculateForShouldCalculateCorrectCostForTwoProductsFromDifferentCategories() {
        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculatorImpl(1, 2, 2.99);

        cart.addItem(audi, 1);

        double deliveryCost = deliveryCostCalculator.calculateFor(cart);

        assertEquals(2.99 + 2 + 2 * 2, deliveryCost, DELTA);
    }

}
