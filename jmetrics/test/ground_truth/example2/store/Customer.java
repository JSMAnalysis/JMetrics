package ground_truth.example2.store;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;
import ground_truth.example2.kitchen.Pizza;

import java.util.ArrayList;
import java.util.List;

@ClassInfo(
        numberOfMethod = 1,
        numberOfAbstractMethod = 0,
        Ca = 1,
        Ce = 2,
        I = 0.67,
        A = 0,
        Dn = 0.33
)
public class Customer {

    private String name;
    @Dependency(dependencyTo = Pizza.class, type = DependencyType.Association, sourceOnly = true)
    private List<Pizza> pizzas;

    public Customer(String name) {
        this.name = name;
        pizzas = new ArrayList<>();
    }

    @Dependency(dependencyTo = Pizza.class, type = DependencyType.UseLink)
    public void addPizzaToOrder(Pizza p) {
        pizzas.add(p);
    }

}
