package fr.ubordeaux.jmetrics.datastructure;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;

public class DependencyEdge extends DirectedGraphEdge<ClassCategory> {

    private DependencyType type;

    public DependencyEdge(ClassCategory source, ClassCategory target, DependencyType type) {
        super(source, target);
        this.type = type;
    }

    public DependencyType getType() {
        return this.type;
    }

}
