package ground_truth.example1;

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
@Dependency(dependencyTo = Vehicle.class, type = DependencyType.Inheritance)
public class Car extends Vehicle {

    @Override
    public void move() {

    }

}
