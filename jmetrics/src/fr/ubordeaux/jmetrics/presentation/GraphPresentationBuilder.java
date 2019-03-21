package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.graph.DependencyEdge;
import fr.ubordeaux.jmetrics.metrics.Granule;

/**
 * Interface that build a representation of a graph as a String.
 */
public interface GraphPresentationBuilder {

    /**
     * Reset the internal structure of the builder, allowing to create a new graph.
     */
    void createNewGraph();

    /**
     * Adds a new node to the graph.
     * IMPORTANT : all nodes must be added before calling any other method of the builder.
     * If another method is called, subsequents calls to this method will do nothing.
     * @param node The node to add.
     */
    void addNode(Granule node);

    /**
     * Adds a new edge to the graph. Source and destination nodes must have been previously added.
     * If not, this method does nothing.
     * @param edge The edge to add.
     */
    void addEdge(DependencyEdge edge);

    /**
     * Called to finish the graph's representation.
     * No node or edge can be added after calling this method.
     */
    void endGraph();

    /**
     * Returns a {@link String} containing the representation of the graph, depending on the chosen builder.
     * IMPORTANT : endGraph must have been called before calling this method, otherwise it returns an empty {@link String}
     * @return The graph's representation.
     */
    String getGraphPresentation();

}
