package fr.ubordeaux.jmetrics.datastructure;

import fr.ubordeaux.jmetrics.analysis.DependencyType;

public class DependencyEdge extends DirectedGraphEdge {

    private DependencyType type;

    public DependencyEdge(DirectedGraphNode source, DirectedGraphNode target, DependencyType type) {
        super(source, target);
        this.type = type;
    }

    public DependencyType getType() {
        return this.type;
    }

}
