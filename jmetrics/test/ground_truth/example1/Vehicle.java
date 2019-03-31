package ground_truth.example1;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

import java.util.ArrayList;

@ClassInfo(
        numberOfMethod = 3,
        numberOfAbstractMethod = 1,
        Ca = 3,
        Ce = 2,
        I = 0.6,
        A = 0.33,
        Dn = 0.12
)
public abstract class Vehicle {

    private int nbWheel;

    @Dependency(dependencyTo = Wheel.class, type = DependencyType.Aggregation, sourceOnly = true)
    private ArrayList<Wheel> wheels;

    public abstract void move();

    public void setNbWheel(int i) {
        this.nbWheel = i;
    }

    @Dependency(dependencyTo = Wheel.class, type = DependencyType.UseLink)
    public void addWheel(Wheel w) {
        this.wheels.add(w);
    }

}