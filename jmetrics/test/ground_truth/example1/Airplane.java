package ground_truth.example1;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

@ClassInfo(
        numberOfMethod = 1,
        numberOfAbstractMethod = 0,
        Ca = 1,
        Ce = 1,
        I = 0.5,
        A = 0,
        Dn = 0.5
)
@Dependency(dependencyTo = Vehicle.class, type = DependencyType.Inheritance)
public class Airplane extends Vehicle {

    @Override
    public void move() {

    }

}
