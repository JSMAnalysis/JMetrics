package ground_truth.example3;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

@ClassInfo(
        numberOfMethod = 0,
        numberOfAbstractMethod = 0,
        Ca = 1,
        Ce = 1,
        I = 0.5,
        A = 0,
        Dn = 0.5
)
public class D {
    @Dependency(dependencyTo = C.class, type = DependencyType.Association)
    private C c;
}
