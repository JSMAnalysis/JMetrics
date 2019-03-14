package ground_truth.example2.kitchen.ingredients;

import ground_truth.ClassInfo;

@ClassInfo(
        numberOfMethod = 1,
        numberOfAbstractMethod = 1,
        Ca = 2,
        Ce = 1,
        I = 0.33,
        A = 0.33,
        Dn = 0.33
)
public abstract class Ingredient {

    private String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public abstract boolean isAvailable();

}
