package ground_truth.example2.kitchen;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;
import ground_truth.example2.kitchen.ingredients.Ingredient;

import java.util.ArrayList;
import java.util.List;

@ClassInfo(
        numberOfMethod = 7,
        numberOfAbstractMethod = 0,
        Ca = 1,
        Ce = 1,
        I = 0.5,
        A = 0,
        Dn = 0.5
)
public class Pizza {

    @Dependency(dependencyTo = BasePizza.class, type = DependencyType.Aggregation)
    private BasePizza base;
    @Dependency(dependencyTo = PizzaSize.class, type = DependencyType.Aggregation)
    private PizzaSize pizzaSize;
    @Dependency(dependencyTo = PastaType.class, type = DependencyType.Aggregation)
    private PastaType pastaType;
    private List<Ingredient> ingredientList;
    private double price;

    public Pizza(BasePizza b, PizzaSize t, PastaType patePizza, double price) {
        this.base = b;
        this.pizzaSize = t;
        this.pastaType = patePizza;
        this.ingredientList = new ArrayList<>();
        this.price = price;
    }

    @Dependency(dependencyTo = Ingredient.class, type = DependencyType.UseLink)
    private void addIngredientToPizza(Ingredient i) {
        ingredientList.add(i);
    }

    public BasePizza getBase() {
        return base;
    }

    public PizzaSize getPizzaSize() {
        return pizzaSize;
    }

    public PastaType getPastaType() {
        return pastaType;
    }

    @Dependency(dependencyTo = BasePizza.class, type = DependencyType.Aggregation)
    public void setBase(BasePizza base) {
        this.base = base;
    }

    @Dependency(dependencyTo = PizzaSize.class, type = DependencyType.Aggregation)
    public void setPizzaSize(PizzaSize pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    @Dependency(dependencyTo = PastaType.class, type = DependencyType.Aggregation)
    public void setPastaType(PastaType pastaType) {
        this.pastaType = pastaType;
    }

}
