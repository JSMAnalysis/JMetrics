package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.metrics.GranularityScale;

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
    private static final String SIGNATURE_COLOR = "green";
    private static final String INTERNAL_COLOR = "grey";

    private StringBuilder strBuilder;
    private Map<GranularityScale, Integer> mapping;
    private int nodeNumber = 0;

    public GraphDotBuilder(){
        createNewGraph();
    }

    @Override
    public void createNewGraph(){
        strBuilder = new StringBuilder();
        mapping = new HashMap<>();
        nodeNumber = 0;
        strBuilder.append(BEGINNING_TOKEN);
        buildLegend();
    }

    @Override
    public void addNode(GranularityScale category){
        mapping.putIfAbsent(category, ++nodeNumber);
        strBuilder.append(nodeNumber);
        strBuilder.append(LABEL_BEGIN);
        strBuilder.append(category.getName());
        strBuilder.append(LABEL_END);
        strBuilder.append(LINE_END_TOKEN);
    }

    @Override
    public void addEdge(DependencyEdge edge){
        strBuilder.append(mapping.get(edge.getSource()));
        strBuilder.append(EDGE_TOKEN);
        strBuilder.append(mapping.get(edge.getTarget()));
        strBuilder.append(" [color=\"");
        //FIXME I don't like switches, but I do not have a better idea right now
        switch (edge.getType()){
            case Inheritance:
                strBuilder.append(INHERITANCE_COLOR);
                break;
            case Aggregation:
                strBuilder.append(AGGREGATION_COLOR);
                break;
            case Signature:
                strBuilder.append(SIGNATURE_COLOR);
                break;
            case Internal:
                strBuilder.append(INHERITANCE_COLOR);
                break;
            default:
                strBuilder.append("black");
                break;
        }
        strBuilder.append("\"]");
        strBuilder.append(LINE_END_TOKEN);
    }

    @Override
    public void endGraph(){
        strBuilder.append(ENDING_TOKEN);
    }

    @Override
    public String getGraphPresentation(){
        return strBuilder.toString();
    }

    private void buildLegend(){
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
        strBuilder.append("g [style=invis];");
        strBuilder.append("h [style=invis];");
        strBuilder.append("g -> h [label=\"Inheritance\", color=\"" + INHERITANCE_COLOR + "\"];");
        strBuilder.append("e -> f [label=\"Aggregation\", color=\"" + AGGREGATION_COLOR + "\"];");
        strBuilder.append("c -> d [label=\"Signature\", color=\"" + SIGNATURE_COLOR + "\"];");
        strBuilder.append("a -> b [label=\"Internal\", color=\"" + INTERNAL_COLOR + "\"];");
        strBuilder.append("}");
    }

}
