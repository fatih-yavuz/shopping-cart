public class DeliveryCostCalculatorFactory {

    private DeliveryCostCalculatorFactory() {
    }

    public static DeliveryCostCalculator createCalculator() {
        DeliveryProperties properties = new DeliveryProperties();
        return new DeliveryCostCalculatorImpl(
                properties.getCostPerDelivery(),
                properties.getCostPerProduct(),
                properties.getFixedCost()
        );
    }
}
