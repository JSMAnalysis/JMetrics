package fr.ubordeaux.jmetrics.datastructure;

import java.util.*;

/**
 * Defines a directed graph data structures with usual primitives.
 */
public class DirectedGraph {
    private Map<DirectedGraphNode, List<DirectedGraphEdge>> adjacencyList;

    public DirectedGraph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Adds a node to the graph if it does not already exist.
     * @param node The node to add.
     */
    public void addNode(DirectedGraphNode node){
        this.adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    /**
     * Removes a node from the graph, as well as all edges that targets this node. If the node does not exist, this
     * function has no effect.
     * @param node The node to remove.
     */
    public void removeNode(DirectedGraphNode node){
        if(!hasNode(node)){
            return;
        }
        //removes all edges which the node is targeted by
        for(List<DirectedGraphEdge> edges : adjacencyList.values()){
            List<DirectedGraphEdge> edgesToRemove = new ArrayList<>();
            for(DirectedGraphEdge edge : edges){
                if(edge.getTarget().equals(node)){
                    edgesToRemove.add(edge);
                }
            }
            edges.removeAll(edgesToRemove);
        }
        //actually removes the node
        adjacencyList.remove(node);
    }

    /**
     * Add a new edge to the graph.
     * @param edge The edge to add.
     * @throws NodeNotFoundException if either the destination or the source of the edge is not part of the graph.
     */
    public void addEdge(DirectedGraphEdge edge){
        if(!hasNode(edge.getSource()) || !hasNode(edge.getTarget())){
            throw new NodeNotFoundException("The edge contains nodes that are not part of the graph");
        }
        adjacencyList.get(edge.getSource()).add(edge);
    }

    /**
     * Add an edge from the graph.
     * @param edge The edge to remove.
     * @throws NodeNotFoundException if either the destination or the source of the edge is not part of the graph.
     */
    public void removeEdge(DirectedGraphEdge edge){
        if(!hasNode(edge.getSource()) || !hasNode(edge.getTarget())){
            throw new NodeNotFoundException("The edge contains nodes that are not part of the graph");
        }
        adjacencyList.get(edge.getSource()).remove(edge);
    }

    /**
     * Tests whether the graph contains a node.
     * @param node The node to test the existence of.
     * @return true if the node exists, false otherwise.
     */
    public boolean hasNode(DirectedGraphNode node){
        return adjacencyList.containsKey(node);
    }

    /**
     * A getter to obtain all the nodes contained in the graph.
     * @return A copy of the {@link Set} of nodes contained in the graph.
     */
    //TODO Définir si on renvoie une copie pour l'encapsulation ou si on renvoie le Set d'origine pour améliorer les performances
    public Set<DirectedGraphNode> getNodeSet(){
        return new HashSet<>(adjacencyList.keySet());
    }

    /**
     * A getter to obtain all the edges which a node is the source of.
     * @param source The node that is the source of the edges.
     * @return A {@link List} containing all the edges coming from the provided node.
     */
    public List<DirectedGraphEdge> getOutcomingEdgeList(DirectedGraphNode source){
        List<DirectedGraphEdge> edges = adjacencyList.get(source);
        if(hasNode(source)){
            return new ArrayList<>(adjacencyList.get(source));
        }
        return null;
    }

    /**
     * A getter to obtain all the edges which a node is the destination of, coming from another specific node.
     * @param destination The node that is the destination of the edges.
     * @param source The node that is the source of the edges.
     * @return A {@link List} containing all the edges coming from the provided source to the provided destination.
     * @throws NodeNotFoundException if either the destination or the source is not part of the graph.
     */
    public List<DirectedGraphEdge> getIncomingEdgesList(DirectedGraphNode destination, DirectedGraphNode source){
        List<DirectedGraphEdge> incomingEdges = new ArrayList<>();
        if(!hasNode(destination) || !hasNode(source)){
            throw new NodeNotFoundException("Either the destination or source node does not exist.");
        }
        for(DirectedGraphEdge edge : adjacencyList.get(source)){
            if(edge.getTarget().equals(destination)){
                incomingEdges.add(edge);
            }
        }
        return incomingEdges;
    }

    /**
     * A getter to obtain all the edges which a node is the destination of, coming from any node.
     * @param destination The node that is the destination of the edges.
     * @return A {@link List} containing all the edges arriving to the provided destination.
     * @throws NodeNotFoundException if the destination is not part of the graph.
     */
    public List<DirectedGraphEdge> getIncomingEdgesList(DirectedGraphNode destination){
        List<DirectedGraphEdge> incomingEdges = new ArrayList<>();
        if(!hasNode(destination)){
            throw new NodeNotFoundException("The destination node does not exist.");
        }
        for(DirectedGraphNode source : adjacencyList.keySet()){
            incomingEdges.addAll(getIncomingEdgesList(destination, source));
        }
        return incomingEdges;
    }

}
