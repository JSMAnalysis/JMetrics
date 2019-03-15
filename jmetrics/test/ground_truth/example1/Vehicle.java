package ground_truth.example1;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import ground_truth.ClassInfo;
import ground_truth.Dependency;

import java.util.ArrayList;

@ClassInfo(
        numberOfMethod = 1,
        numberOfAbstractMethod = 1,
        Ca = 2,
        Ce = 1,
        I = 0.33,
        A = 1,
        Dn = 0.33
)
public abstract class Vehicle {

    private int nbWheel;

    private ArrayList<Wheel> wheels;

    public abstract void move();

}
