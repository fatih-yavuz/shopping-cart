import java.util.Comparator;

public class DiscountStrategyComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof RateDiscountStrategy && o2 instanceof RateDiscountStrategy) {
            return 0;
        }
        if (o1 instanceof RateDiscountStrategy) {
            return 1;
        }
        if (o2 instanceof RateDiscountStrategy) {
            return -1;
        }
        return 0;
    }
}
