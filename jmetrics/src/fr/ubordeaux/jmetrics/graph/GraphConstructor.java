package fr.ubordeaux.jmetrics.graph;

import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.metrics.Granule;
import fr.ubordeaux.jmetrics.presentation.GraphDotBuilder;
import fr.ubordeaux.jmetrics.presentation.GraphPresentationBuilder;

import java.util.List;
import java.util.Set;

/**
 * Service that provide graph construction and presentation methods.
 */
public final class GraphConstructor {

    // Private constructor to prevent instantiation
    private GraphConstructor() { }

    public static DirectedGraph<Granule, DependencyEdge> constructGraph(Set<Granule> nodes, Set<Dependency> dependencies) {
        DirectedGraph<Granule, DependencyEdge> graph = new DirectedGraph<>();
        for (Granule node : nodes) {
            graph.addNode(node);
        }
        for (Dependency dependency : dependencies) {
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

    public static int[][] getMatrixRepresentation(List<Granule> granules, DirectedGraph<Granule, DependencyEdge> graph) {
        int size = granules.size();
        int[][] matrix = new int[size][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = new int[size];
        }

        List<DependencyEdge> incomingEdges;
        int i1, i2;
        for (Granule g: granules) {
            i2 = granules.indexOf(g);
            incomingEdges = graph.getIncomingEdgesList(g);
            for (DependencyEdge e: incomingEdges) {
                i1 = granules.indexOf(e.getSource());
                matrix[i1][i2]++; // Or = 1, if we don't want to keep DependencyType information.
            }
        }

        return matrix;
    }

}
