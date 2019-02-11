package fr.ubordeaux.jmetrics.datastructure;

import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;

import java.util.Set;

/**
 * Service that construct a graph from a set of dependencies.
 */
public class GraphConstructor {

    public DirectedGraph<ClassCategory, DependencyEdge> constructGraph(Set<ClassCategory> categories, Set<Dependency> dependencies) {
        DirectedGraph<ClassCategory, DependencyEdge> graph = new DirectedGraph<>();

        for (ClassCategory category : categories) {
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
