package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.metrics.GranularityScale;

/**
 * Exposes an interface to build the representation of a graph as a String
 */
public interface GraphPresentationBuilder {

    void createNewGraph();

    void addNode(GranularityScale category);

    void addEdge(DependencyEdge edge);

    void endGraph();

    String getGraphPresentation();

}
