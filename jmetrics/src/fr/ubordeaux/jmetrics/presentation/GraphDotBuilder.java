package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;

import java.util.Map;

/**
 * Builds the representation of a graph using DOT format.
 */
public class GraphDotBuilder implements GraphPresentationBuilder {

    private static final String BEGINNING_TOKEN = "digraph jmetrics_graph { ";
    private static final String ENDING_TOKEN = "}";
    private static final String LINEEND_TOKEN = ";";
    private static final String LABEl_BEGIN = "[label=\"";
    private static final String LABEL_END = "\"]";
    private static final String EDGE_TOKEN = " -> ";

    private StringBuilder strBuilder;
    private Map<ClassCategory, Integer> mapping;
    private int nodeNumber = 0;

    public GraphDotBuilder(){
        strBuilder = new StringBuilder();
    }

    @Override
    public void createNewGraph(){
        strBuilder = new StringBuilder();
        nodeNumber = 0;
        strBuilder.append(BEGINNING_TOKEN);
    }

    @Override
    public void addNode(ClassCategory category){
        mapping.putIfAbsent(category, ++nodeNumber);
        strBuilder.append(nodeNumber);
        strBuilder.append(LABEl_BEGIN);
        strBuilder.append(category.getName());
        strBuilder.append(LABEL_END);
        strBuilder.append(LINEEND_TOKEN);
    }

    @Override
    public void addEdge(DependencyEdge edge){
        strBuilder.append(mapping.get(edge.getSource()));
        strBuilder.append(EDGE_TOKEN);
        strBuilder.append(mapping.get(edge.getTarget()));
        strBuilder.append(LINEEND_TOKEN);
    }

    @Override
    public void endGraph(){
        strBuilder.append(ENDING_TOKEN);
    }

    @Override
    public String getGraphPresentation(){
        return strBuilder.toString();
    }

}
