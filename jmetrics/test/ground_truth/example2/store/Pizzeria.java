package ground_truth.example2.store;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

import java.util.List;

@ClassInfo(
        numberOfMethod = 1,
        numberOfAbstractMethod = 0,
        Ca = 0,
        Ce = 2,
        I = 1,
        A = 0,
        Dn = 0
)
public class Pizzeria {

    @Dependency(dependencyTo = Pizzaiolo.class, type = DependencyType.Association, sourceOnly = true)
    private List<Pizzaiolo> employees;
    @Dependency(dependencyTo = Customer.class, type = DependencyType.Association, sourceOnly = true)
    private List<Customer> customers;

    public void run () {  }

}
