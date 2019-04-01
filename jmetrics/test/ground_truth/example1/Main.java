package ground_truth.example1;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

@ClassInfo(
        numberOfMethod = 1,
        numberOfAbstractMethod = 0,
        Ca = 0,
        Ce = 4,
        I = 1,
        A = 0,
        Dn = 0
)
public class Main {

    @Dependency(dependencyTo = Vehicle.class, type = DependencyType.UseLink, sourceOnly = true)
    @Dependency(dependencyTo = Airplane.class, type = DependencyType.UseLink, sourceOnly = true)
    @Dependency(dependencyTo = Wheel.class, type = DependencyType.UseLink, sourceOnly = true)
    @Dependency(dependencyTo = Material.class, type = DependencyType.UseLink, sourceOnly = true)
    public static void main(String[] args) {
        Vehicle v = new Airplane();
        v.setNbWheel(1);
        Wheel w = new Wheel();
        w.setMaterial(Material.Plastic);
        v.addWheel(w);
    }

}
