package ground_truth.example3;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

@ClassInfo(
        numberOfMethod = 0,
        numberOfAbstractMethod = 0,
        Ca = 0,
        Ce = 1,
        I = 1,
        A = 0,
        Dn = 0
)
@Dependency(dependencyTo = E.class, type = DependencyType.Inheritance)
public abstract class F extends E{
}
