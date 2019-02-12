package ground_truth.example2.domain.kitchen.ingredients;

public class Tomato extends Ingredient {
    public Tomato(String name) {
        super(name);
    }

    @Override
    public boolean isAvailable() {
        return false;
    }
}
