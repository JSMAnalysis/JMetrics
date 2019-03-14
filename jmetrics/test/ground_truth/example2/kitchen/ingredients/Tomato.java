package ground_truth.example2.kitchen.ingredients;


import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

@ClassInfo(
        numberOfMethod = 1,
        numberOfAbstractMethod = 0,
        Ca = 0,
        Ce = 1,
        I = 1,
        A = 0,
        Dn = 0
)
@Dependency(dependencyTo = Ingredient.class, type = DependencyType.Inheritance)
public class Tomato extends Ingredient {

    public Tomato(String name) {
        super(name);
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

}
