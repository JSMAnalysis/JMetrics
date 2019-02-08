package fr.ubordeaux.jmetrics.datastructure;

import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Service that construct a graph from a set of dependencies.
 */
public class GraphConstructor {

    public DirectedGraph constructGraph(Set<ClassCategory> categories, Set<Dependency> dependencies) {
        DirectedGraph graph = new DirectedGraph();
        Map<ClassCategory, DirectedGraphNode> nodes = new HashMap<>();

        for (ClassCategory category : categories) {
            DirectedGraphNode node = new DirectedGraphNode(category.getName());
            nodes.put(category, node);
            graph.addNode(node);
        }

        for(Dependency dependency : dependencies) {
            DirectedGraphEdge edge = new DependencyEdge(
                    nodes.get(dependency.getSource()),
                    nodes.get(dependency.getDestination()),
                    dependency.getType()
            );
            graph.addEdge(edge);
        }

        return graph;
    }

}
