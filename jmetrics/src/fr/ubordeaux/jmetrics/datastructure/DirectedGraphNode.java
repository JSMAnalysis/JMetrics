package fr.ubordeaux.jmetrics.datastructure;

/**
 * Defines the base data structure used to store information about a directed graph's node.
 */
public class DirectedGraphNode {
    private String label;

    public DirectedGraphNode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DirectedGraphNode)){
            return false;
        }
        DirectedGraphNode other = (DirectedGraphNode) obj;
        return this.label.equals(other.label);
    }
}
