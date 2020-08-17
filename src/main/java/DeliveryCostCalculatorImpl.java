class DeliveryCostCalculatorImpl implements DeliveryCostCalculator {
    private double costPerDelivery;
    private double costPerProduct;
    private double fixedCost;

    DeliveryCostCalculatorImpl(double costPerDelivery, double costPerProduct, double fixedCost) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
        this.fixedCost = fixedCost;
    }

    public double calculateFor(ShoppingCart cart) {
        int numOfDeliveries = cart.getNumberOfDeliveries();
        int numOfProducts = cart.getNumberOfProducts();
        return (costPerDelivery * numOfDeliveries) + (costPerProduct * numOfProducts) + fixedCost;
    }

}
