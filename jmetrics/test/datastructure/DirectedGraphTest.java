package datastructure;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;
import fr.ubordeaux.jmetrics.datastructure.GraphConstructor;
import fr.ubordeaux.jmetrics.metrics.ClassGranularity;
import fr.ubordeaux.jmetrics.metrics.GranularityScale;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.FileSystemExplorer;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import ground_truth.GroundTruthManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectedGraphTest {

    @BeforeEach
    void setUp() {
        ProjectStructure structure = ProjectStructure.getInstance();
        FileSystemExplorer explorer = new FileSystemExplorer();
        GroundTruthManager GT = new GroundTruthManager();
        String path = GT.getProject(1).getPath();
        structure.setStructure(explorer.generateStructure(path));
    }

    private DirectedGraph<GranularityScale, DependencyEdge> initGraph() {
        Set<GranularityScale> nodes = new HashSet<>();
        List<ClassFile> classes = ProjectStructure.getInstance().getClasses();

        for (ClassFile c : classes) {
            nodes.add(new ClassGranularity(c));
        }
        return (new GraphConstructor()).constructGraph(nodes, new HashSet<>());
    }

    @Test
    void testCorrectSizeOfGraph() {
        DirectedGraph<GranularityScale, DependencyEdge> graph = initGraph();
        assertEquals(ProjectStructure.getInstance().getClasses().size(), graph.getNodeSet().size());

        graph.removeNode(graph.getNodeSet().iterator().next());
        assertEquals(ProjectStructure.getInstance().getClasses().size() - 1 , graph.getNodeSet().size());
    }

    @Test
    void testHasNodeInGraph() {
        DirectedGraph<GranularityScale, DependencyEdge> graph = initGraph();
        assertTrue(!graph.getNodeSet().isEmpty());
    }

    @Test
    void testAddNodeInGraph() {
        DirectedGraph<GranularityScale, DependencyEdge> graph = initGraph();
        DirectedGraph<GranularityScale, DependencyEdge> graphBis = new DirectedGraph<>();

        for (GranularityScale gScale : graph.getNodeSet()) {
            graphBis.addNode(gScale);
        }

        assertEquals(graph.getNodeSet().size(), graphBis.getNodeSet().size());
        assertEquals(ProjectStructure.getInstance().getClasses().size(), graphBis.getNodeSet().size());
    }

}