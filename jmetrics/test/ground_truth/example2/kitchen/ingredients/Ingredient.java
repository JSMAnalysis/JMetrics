package ground_truth.example2.kitchen.ingredients;

import ground_truth.ClassInfo;

@ClassInfo(
        numberOfMethod = 1,
        numberOfAbstractMethod = 1,
        Ca = 4,
        Ce = 0,
        I = 0,
        A = 1,
        Dn = 0
)
public abstract class Ingredient {

    private String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public abstract boolean isAvailable();

}
