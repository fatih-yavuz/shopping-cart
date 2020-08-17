import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest(DeliveryProperties.class)
public class DeliveryPropertiesTest extends BaseTest {

    @Test
    public void initializesWhenThereIsNoFile() {
        PowerMockito.mockStatic(DeliveryProperties.class);
        BDDMockito.given(DeliveryProperties.getPath()).willReturn("some/path/does/not/exists");
        DeliveryProperties properties = new DeliveryProperties();
        assertEquals(0, properties.getCostPerDelivery(), DELTA);
        assertEquals(0, properties.getCostPerProduct(), DELTA);
        assertEquals(0, properties.getFixedCost(), DELTA);
    }
}
