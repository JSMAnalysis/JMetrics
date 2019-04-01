package ground_truth.example1;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

@ClassInfo(
        numberOfMethod = 2,
        numberOfAbstractMethod = 0,
        Ca = 3,
        Ce = 2,
        I = 0.4,
        A = 0,
        Dn = 0.6
)
public class Wheel {

    @Dependency(dependencyTo = Material.class, type = DependencyType.Association)
    private Material material;

    @Dependency(dependencyTo = Material.class, type = DependencyType.UseLink)
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material m) {
        this.material = m;
    }

}
