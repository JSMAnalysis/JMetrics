package fr.ubordeaux.jmetrics.metrics.components;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;

public class EfferentCoupling extends MetricsComponent {

    public EfferentCoupling(DirectedGraph<ClassCategory, DependencyEdge> graph, ClassCategory category) {
        super(graph.getOutcomingEdgeList(category).size());
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value == Math.floor(value);
    }

}
