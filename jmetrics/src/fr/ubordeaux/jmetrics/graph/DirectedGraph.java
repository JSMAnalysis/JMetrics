package fr.ubordeaux.jmetrics.graph;

import java.util.*;

/**
 * Defines a directed graph data structures with usual primitives.
 */
public class DirectedGraph<N, E extends DirectedGraphEdge<N>> {

    private Map<N, List<E>> adjacencyLists;

    public DirectedGraph() {
        adjacencyLists = new HashMap<>();
    }

    /**
     * Adds a node to the graph if it does not already exist.
     * @param node The node to add.
     */
    public void addNode(N node) {
        adjacencyLists.putIfAbsent(node, new ArrayList<>());
    }

    /**
     * Add a new edge to the graph.
     * @param edge The edge to add.
     * @throws NodeNotFoundException if either the destination or the source of the edge is not part of the graph.
     */
    public void addEdge(E edge) {
        if (!hasNode(edge.getSource()) || !hasNode(edge.getTarget())) {
            throw new NodeNotFoundException("The edge contains nodes that are not part of the graph");
        }
        adjacencyLists.get(edge.getSource()).add(edge);
    }

    /**
     * Tests whether the graph contains a node.
     * @param node The node to test the existence of.
     * @return true if the node exists, false otherwise.
     */
    private boolean hasNode(N node) {
        return adjacencyLists.containsKey(node);
    }

    /**
     * A getter to obtain all the nodes contained in the graph.
     * @return A copy of the {@link Set} of nodes contained in the graph.
     */
    public Set<N> getNodeSet() {
        return new HashSet<>(adjacencyLists.keySet());
    }

    /**
     * A getter to obtain all edges which a node is the source of.
     * @param source The node that is the source of the edges.
     * @return A {@link List} containing all the edges coming from the provided node.
     */
    public List<E> getOutcomingEdgeList(N source) {
        return hasNode(source) ? new ArrayList<>(adjacencyLists.get(source)) : new ArrayList<>();
    }

    /**
     * A getter to obtain all edges which a node is the destination of, coming from another specific node.
     * @param destination The node that is the destination of the edges.
     * @param source The node that is the source of the edges.
     * @return A {@link List} containing all edges coming from the provided source to the provided destination.
     * @throws NodeNotFoundException if either the destination or the source is not part of the graph.
     */
    private List<E> getIncomingEdgesList(N destination, N source) {
        List<E> incomingEdges = new ArrayList<>();
        if (!hasNode(destination) || !hasNode(source)){
            throw new NodeNotFoundException("Either the destination or source node does not exist.");
        }
        for (E edge : adjacencyLists.get(source)) {
            if (edge.getTarget().equals(destination)) {
                incomingEdges.add(edge);
            }
        }
        return incomingEdges;
    }

    /**
     * A getter to obtain all edges which a node is the destination of, coming from any node.
     * @param destination The node that is the destination of edges.
     * @return A {@link List} containing all edges arriving to the provided destination.
     * @throws NodeNotFoundException if the destination is not part of the graph.
     */
    public List<E> getIncomingEdgesList(N destination) {
        List<E> incomingEdges = new ArrayList<>();
        if (!hasNode(destination)) {
            throw new NodeNotFoundException("The destination node does not exist.");
        }
        for (N source : adjacencyLists.keySet()) {
            incomingEdges.addAll(getIncomingEdgesList(destination, source));
        }
        return incomingEdges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectedGraph<?, ?> that = (DirectedGraph<?, ?>) o;
        return adjacencyLists.equals(that.adjacencyLists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adjacencyLists);
    }

}
