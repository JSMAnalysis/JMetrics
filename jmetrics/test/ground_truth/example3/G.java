package ground_truth.example3;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

@ClassInfo(
        numberOfMethod = 1,
        numberOfAbstractMethod = 1,
        Ca = 0,
        Ce = 1,
        I = 1,
        A = 1,
        Dn = 1
)
@Dependency(dependencyTo = E.class, type = DependencyType.Inheritance)
public abstract class G extends E {
    public abstract void doSomething();
}
