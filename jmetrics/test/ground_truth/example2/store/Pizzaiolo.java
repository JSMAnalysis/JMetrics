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
        Ce = 1,
        I = 0.5,
        A = 0,
        Dn = 0.5
)
public class Pizzaiolo {

    @Dependency(dependencyTo = Pizza.class, type = DependencyType.Aggregation, sourceOnly = true)
    private List<Pizza> pizzaToPrepare;

    public Pizzaiolo() {
        this.pizzaToPrepare = new ArrayList<>();
    }

    @Dependency(dependencyTo = Pizza.class, type = DependencyType.UseLink)
    public void addPizzaToPrepare(Pizza p) {
        pizzaToPrepare.add(p);
    }

}
