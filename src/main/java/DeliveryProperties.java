import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DeliveryProperties {
    private double costPerDelivery;
    private double costPerProduct;
    private double fixedCost;

    public DeliveryProperties() {
        try {
            FileReader reader = new FileReader(getPath());
            Properties properties = new Properties();
            properties.load(reader);
            costPerDelivery = Double.parseDouble(properties.getProperty("costPerDelivery"));
            costPerProduct = Double.parseDouble(properties.getProperty("costPerProduct"));
            fixedCost = Double.parseDouble(properties.getProperty("fixedCost"));
        } catch (IOException e) {
            System.out.println("An exception occured while reading delivery properties");
        }
    }


    public double getCostPerDelivery() {
        return costPerDelivery;
    }

    public double getCostPerProduct() {
        return costPerProduct;
    }

    public double getFixedCost() {
        return fixedCost;
    }

    public static String getPath() {
        return "delivery.properties";
    }
}
