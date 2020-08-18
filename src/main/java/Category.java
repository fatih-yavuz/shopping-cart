public class Category {
    private String title;
    private Category parent;

    public Category(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
