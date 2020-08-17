import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DeliveryProperties.class)
public class DeliveryCostCalculatorFactoryTest extends BaseTest {

    @Test
    public void createCalculator() {
        Category food = new Category("food");
        Product apple = new Product("apple", 5, food);
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(apple, 4);


        PowerMockito.mockStatic(DeliveryProperties.class);
        BDDMockito.given(DeliveryProperties.getPath()).willReturn("some/path/does/not/exists");
        DeliveryCostCalculator deliveryCostCalculator = DeliveryCostCalculatorFactory.createCalculator();

        double cost = deliveryCostCalculator.calculateFor(cart);
        assertEquals(0, cost, DELTA);
    }
}
