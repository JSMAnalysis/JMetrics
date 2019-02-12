package ground_truth.example2.domain.kitchen.ingredients;

public class Pickles extends Ingredient {
    public Pickles(String name) {
        super(name);
    }

    @Override
    public boolean isAvailable() {
        return false;
    }
}
