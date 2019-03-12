package datastructure;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;
import fr.ubordeaux.jmetrics.datastructure.GraphConstructor;
import fr.ubordeaux.jmetrics.metrics.ClassGranule;
import fr.ubordeaux.jmetrics.metrics.Granule;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import ground_truth.GroundTruthManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectedGraphTest {

    private DirectedGraph<Granule, DependencyEdge> graph;
    private List<ClassFile> classes;

    @BeforeEach
    void setUp() {
        GroundTruthManager GT = new GroundTruthManager();
        GT.loadExample(2);
        classes = ProjectStructure.getInstance().getClasses();
        Set<Granule> nodes = new HashSet<>();
        for (ClassFile c: classes) {
            System.out.println(c.getName());
            nodes.add(new ClassGranule(c));
        }
        graph = GraphConstructor.constructGraph(nodes, new HashSet<>());
    }

    @Test
    void testCorrectSizeOfGraph() {
        assertEquals(classes.size(), graph.getNodeSet().size());
        graph.removeNode(graph.getNodeSet().iterator().next());
        assertEquals(classes.size() - 1, graph.getNodeSet().size());
    }

    @Test
    void testAddNodeInGraph() {
        DirectedGraph<Granule, DependencyEdge> graphBis = new DirectedGraph<>();
        for (Granule g: graph.getNodeSet()) {
            graphBis.addNode(g);
        }
        assertEquals(graph.getNodeSet().size(), graphBis.getNodeSet().size());
        assertEquals(classes.size(), graphBis.getNodeSet().size());
    }

}