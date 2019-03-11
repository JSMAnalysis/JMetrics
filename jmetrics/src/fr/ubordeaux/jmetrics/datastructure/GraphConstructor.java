package fr.ubordeaux.jmetrics.datastructure;

import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.metrics.Granule;
import fr.ubordeaux.jmetrics.presentation.GraphDotBuilder;
import fr.ubordeaux.jmetrics.presentation.GraphPresentationBuilder;

import java.util.Set;

/**
 * Service that provide graph construction and presentation methods.
 */
public abstract class GraphConstructor {

    public static DirectedGraph<Granule, DependencyEdge> constructGraph(Set<Granule> nodes, Set<Dependency> dependencies) {
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

    public static String getDOTRepresentation(DirectedGraph<Granule, DependencyEdge> graph) {
        GraphPresentationBuilder gBuilder = new GraphDotBuilder();
        gBuilder.createNewGraph();
        for (Granule node : graph.getNodeSet()) {
            gBuilder.addNode(node);
        }
        for (Granule node : graph.getNodeSet()) {
            for (DependencyEdge edge : graph.getOutcomingEdgeList(node)) {
                gBuilder.addEdge(edge);
            }
        }
        gBuilder.endGraph();
        return gBuilder.getGraphPresentation();
    }

}
