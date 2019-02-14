package fr.ubordeaux.jmetrics.metrics.components;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;
import fr.ubordeaux.jmetrics.metrics.GranularityScale;

public class AfferentCoupling extends MetricsComponent {

    public AfferentCoupling(DirectedGraph<GranularityScale, DependencyEdge> graph, GranularityScale granule) {
        super(graph.getIncomingEdgesList(granule).size());
    }

    @Override
    protected boolean isValid(double value) {
        return value >= 0 && value == Math.floor(value);
    }

}
