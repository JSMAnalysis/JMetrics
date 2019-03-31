package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.graph.DependencyEdge;
import fr.ubordeaux.jmetrics.metrics.Granule;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds the representation of a graph using DOT format.
 */
public class GraphDotBuilder implements GraphPresentationBuilder {

    private static final String BEGINNING_TOKEN = "digraph jmetrics_graph { ";
    private static final String ENDING_TOKEN = "}";
    private static final String LINE_END_TOKEN = ";";
    private static final String LABEL_BEGIN = "[label=\"";
    private static final String LABEL_END = "\"]";
    private static final String EDGE_TOKEN = " -> ";

    private static final String INHERITANCE_COLOR = "red";
    private static final String AGGREGATION_COLOR = "blue";
    private static final String USELINK_COLOR = "green";
    private static final String DEFAULT_COLOR = "black";

    private StringBuilder strBuilder;
    private Map<Granule, Integer> mapping;
    private int nodeNumber;
    private boolean canAddNode;
    private boolean terminated;

    public GraphDotBuilder() {
        createNewGraph();
    }

    @Override
    public void createNewGraph() {
        strBuilder = new StringBuilder();
        mapping = new HashMap<>();
        nodeNumber = 0;
        canAddNode = true;
        terminated = false;
        strBuilder.append(BEGINNING_TOKEN);
        buildLegend();
    }

    @Override
    public void addNode(Granule node) {
        if (!canAddNode || terminated) return;
        mapping.putIfAbsent(node, ++nodeNumber);
        strBuilder.append(nodeNumber);
        strBuilder.append(LABEL_BEGIN);
        strBuilder.append(node.getDisplayName());
        strBuilder.append(LABEL_END);
        strBuilder.append(LINE_END_TOKEN);
    }

    @Override
    public void addEdge(DependencyEdge edge) {
        if (terminated) return;
        strBuilder.append(mapping.get(edge.getSource()));
        strBuilder.append(EDGE_TOKEN);
        strBuilder.append(mapping.get(edge.getTarget()));
        strBuilder.append(" [color=\"");
        switch (edge.getType()){
            case Inheritance:
                strBuilder.append(INHERITANCE_COLOR);
                break;
            case Association:
                strBuilder.append(AGGREGATION_COLOR);
                break;
            case UseLink:
                strBuilder.append(USELINK_COLOR);
                break;
            default:
                strBuilder.append(DEFAULT_COLOR);
                break;
        }
        strBuilder.append("\"]");
        strBuilder.append(LINE_END_TOKEN);
        canAddNode = false;
    }

    @Override
    public void endGraph() {
        if (terminated) return;
        strBuilder.append(ENDING_TOKEN);
        terminated = true;
    }

    @Override
    public String getGraphPresentation() {
        return terminated ? strBuilder.toString() : "";
    }

    private void buildLegend() {
        strBuilder.append("subgraph cluster_legend { ");
        strBuilder.append("label=\"Legend\";");
        strBuilder.append("shape=rectangle;");
        strBuilder.append("color=black;");
        strBuilder.append("a [style=invis];");
        strBuilder.append("b [style=invis];");
        strBuilder.append("c [style=invis];");
        strBuilder.append("d [style=invis];");
        strBuilder.append("e [style=invis];");
        strBuilder.append("f [style=invis];");
        strBuilder.append("e -> f [label=\"Inheritance\", color=\"" + INHERITANCE_COLOR + "\"];");
        strBuilder.append("c -> d [label=\"Association\", color=\"" + AGGREGATION_COLOR + "\"];");
        strBuilder.append("a -> b [label=\"UseLink\", color=\"" + USELINK_COLOR + "\"];");
        strBuilder.append("}");
    }

}
