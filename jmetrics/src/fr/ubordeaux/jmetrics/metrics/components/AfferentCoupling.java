package fr.ubordeaux.jmetrics.metrics.components;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;

public class AfferentCoupling extends MetricsComponent {

    public AfferentCoupling(DirectedGraph<ClassCategory, DependencyEdge> graph, ClassCategory category) {
        super(graph.getIncomingEdgesList(category).size());
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value == Math.floor(value);
    }

}
