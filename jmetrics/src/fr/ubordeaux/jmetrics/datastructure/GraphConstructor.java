package fr.ubordeaux.jmetrics.datastructure;

import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.metrics.Granule;

import java.util.Set;

/**
 * Service that construct a graph from a set of dependencies.
 */
public class GraphConstructor {

    public DirectedGraph<Granule, DependencyEdge> constructGraph(Set<Granule> nodes, Set<Dependency> dependencies) {
        DirectedGraph<Granule, DependencyEdge> graph = new DirectedGraph<>();

        for (Granule node : nodes) {
            graph.addNode(node);
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
