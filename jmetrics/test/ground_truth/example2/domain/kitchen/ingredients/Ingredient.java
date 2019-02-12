package ground_truth.example2.domain.kitchen.ingredients;

public abstract class Ingredient {
    private String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public abstract boolean isAvailable();


}
