package fr.ubordeaux.jmetrics.datastructure;

import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.metrics.GranularityScale;

import java.util.Set;

/**
 * Service that construct a graph from a set of dependencies.
 */
public class GraphConstructor {

    public DirectedGraph<GranularityScale, DependencyEdge> constructGraph(Set<GranularityScale> categories, Set<Dependency> dependencies) {
        DirectedGraph<GranularityScale, DependencyEdge> graph = new DirectedGraph<>();

        for (GranularityScale category : categories) {
            graph.addNode(category);
        }

        for(Dependency dependency : dependencies) {
            DependencyEdge edge = new DependencyEdge(
                    dependency.getSource(),
                    dependency.getDestination(),
                    dependency.getType()
            );
            graph.addEdge(edge);
        }

        return graph;
    }

}
