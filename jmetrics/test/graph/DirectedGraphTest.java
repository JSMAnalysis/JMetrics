package graph;

import fr.ubordeaux.jmetrics.graph.DependencyEdge;
import fr.ubordeaux.jmetrics.graph.DirectedGraph;
import fr.ubordeaux.jmetrics.graph.DirectedGraphEdge;
import fr.ubordeaux.jmetrics.graph.GraphConstructor;
import fr.ubordeaux.jmetrics.metrics.ClassGranule;
import fr.ubordeaux.jmetrics.metrics.Granule;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;
import ground_truth.GroundTruthManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectedGraphTest {

    @Test
    void testGroundTruthGraphNodesValidity() {
        GroundTruthManager GT = new GroundTruthManager();
        for (int projectNumber = 1; projectNumber <= GroundTruthManager.groundTruthSize; ++projectNumber) {
            GT.loadExampleBytecode(projectNumber);
            List<ClassFile> classes = ProjectStructure.getInstance().getClasses();
            Set<Granule> nodes = new HashSet<>();
            for (ClassFile c: classes) {
                nodes.add(new ClassGranule(c));
            }
            DirectedGraph<Granule, DependencyEdge> graph = GraphConstructor.constructGraph(nodes, new HashSet<>());
            assertEquals(classes.size(), graph.getNodeSet().size());

            DirectedGraph<Granule, DependencyEdge> graphBis = new DirectedGraph<>();
            for (Granule g: graph.getNodeSet()) {
                graphBis.addNode(g);
            }
            assertEquals(graph.getNodeSet().size(), graphBis.getNodeSet().size());
            assertEquals(classes.size(), graphBis.getNodeSet().size());
        }

    }



    @Test
    void testGenericGraphEdgesValidity() {

        class GranuleTest { }

        int arraySize = 10;
        DirectedGraph<GranuleTest, DirectedGraphEdge<GranuleTest>> graphTest = new DirectedGraph<>();
        GranuleTest[] granules = new GranuleTest[arraySize];
        for (int i = 0; i < arraySize; i++) {
            granules[i] = new GranuleTest();
            graphTest.addNode(granules[i]);
        }


        List<DirectedGraphEdge<GranuleTest>> edges = new ArrayList<>();
        List<DirectedGraphEdge<GranuleTest>> coming;
        edges.add(new DirectedGraphEdge<>(granules[2], granules[7]));
        edges.add(new DirectedGraphEdge<>(granules[5], granules[7]));
        edges.add(new DirectedGraphEdge<>(granules[8], granules[7]));
        for (DirectedGraphEdge<GranuleTest> e: edges) graphTest.addEdge(e);

        coming = graphTest.getIncomingEdgesList(granules[7]);
        for (DirectedGraphEdge e: coming) assertTrue(edges.contains(e));


        edges.clear();
        edges.add(new DirectedGraphEdge<>(granules[6], granules[2]));
        edges.add(new DirectedGraphEdge<>(granules[6], granules[5]));
        edges.add(new DirectedGraphEdge<>(granules[6], granules[8]));
        for (DirectedGraphEdge<GranuleTest> e: edges) graphTest.addEdge(e);
        coming = graphTest.getOutcomingEdgeList(granules[6]);

        for (DirectedGraphEdge e: coming) assertTrue(edges.contains(e));
        assertEquals(3, coming.size());

    }

}