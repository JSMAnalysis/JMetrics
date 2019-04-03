package metrics;

import fr.ubordeaux.jmetrics.analysis.AbstractnessData;
import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.analysis.JDTParserFactory;
import fr.ubordeaux.jmetrics.analysis.ParserFactory;
import fr.ubordeaux.jmetrics.graph.DependencyEdge;
import fr.ubordeaux.jmetrics.graph.DirectedGraph;
import fr.ubordeaux.jmetrics.graph.GraphConstructor;
import fr.ubordeaux.jmetrics.metrics.*;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.PackageDirectory;
import fr.ubordeaux.jmetrics.project.ProjectStructure;
import fr.ubordeaux.jmetrics.project.SourceFileSystemExplorer;
import ground_truth.ClassInfo;
import ground_truth.GroundTruthManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MartinMetricsTest {

    private MartinMetrics martinMetrics;
    private Method abstractnessMethod, instabilityMethod, normalizedDistanceMethod;

    private SourceFileSystemExplorer explorer;
    private GroundTruthManager GT;
    private ParserFactory parserFactory;

    private GranuleManager granuleManager;
    private List<ClassFile> classes;
    private List<PackageDirectory> packages;

    @BeforeEach
    void setUp() {
        martinMetrics = new MartinMetrics();
        try {
            abstractnessMethod = MartinMetrics.class.getDeclaredMethod("setAbstractness", AbstractnessData.class);
            instabilityMethod = MartinMetrics.class.getDeclaredMethod("setInstability", int.class, int.class);
            normalizedDistanceMethod = MartinMetrics.class.getDeclaredMethod("setNormalizedDistance", double.class, double.class);
            abstractnessMethod.setAccessible(true);
            instabilityMethod.setAccessible(true);
            normalizedDistanceMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSetMetricsRightValue() {
        try {
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(0, 0));
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(10, 0));
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(10, 10));
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(100, 63));

            instabilityMethod.invoke(martinMetrics, 0, 0);
            instabilityMethod.invoke(martinMetrics, 0, 10);
            instabilityMethod.invoke(martinMetrics, 10, 0);
            instabilityMethod.invoke(martinMetrics, 100, 63);

            normalizedDistanceMethod.invoke(martinMetrics, 0, 1);
            normalizedDistanceMethod.invoke(martinMetrics, 0, 0);
            normalizedDistanceMethod.invoke(martinMetrics, 1, 0.63);
        } catch (BadMetricsValueException e) {
            fail("No exception should be throw when MartinMetrics is built with correct values");
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("(TEST ERROR) Error had been raised while trying to access private method through test: ");
            e.printStackTrace();
        }
    }

    @Test
    void testSetMetricsWrongValue() {
        try {
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(10, 12));
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }
        try {
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(-10, 5));
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }

        try {
            instabilityMethod.invoke(martinMetrics, 10, -5);
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }
        try {
            instabilityMethod.invoke(martinMetrics, -5, 10);
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }

        try {
            normalizedDistanceMethod.invoke(martinMetrics, 2, 2);
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }
        try {
            normalizedDistanceMethod.invoke(martinMetrics, -1, -1);
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }
    }

    private void initGranuleManager(int projectNumber) {
        GT = new GroundTruthManager();
        parserFactory = new JDTParserFactory();
        GT.loadExampleSourcecode(projectNumber);
        classes = ProjectStructure.getInstance().getClasses();
        packages = ProjectStructure.getInstance().getPackages();
        granuleManager = new GranuleManager(classes, packages);
    }

    @Test
    void testMartinMetricsOnClass() throws ClassNotFoundException {
        for (int projectNumber = 1; projectNumber <= GroundTruthManager.groundTruthSize; ++projectNumber) {
            initGranuleManager(projectNumber);

            Map<ClassFile, AbstractnessData> aData = new HashMap<>();
            List<Dependency> classDependencies = new ArrayList<>();

            for (ClassFile c : classes) {
                aData.put(c, parserFactory.getAbstractnessParser().getAbstractnessData(c));
                classDependencies.addAll(parserFactory.getCouplingParser().getDependencies(c));
            }

            Set<Granule> classNodes = granuleManager.getClassGranules();

            DirectedGraph<Granule, DependencyEdge> classGraph;
            classGraph = GraphConstructor.constructGraph(classNodes, new HashSet<>(classDependencies));

            for (Granule g : classNodes) {
                MartinMetrics metrics = new MartinMetrics();

                ClassFile classFile = (ClassFile) g.getRelatedComponent();
                metrics.computeClassMetrics((ClassGranule) g, aData.get(classFile), classGraph);
                g.setMetrics(metrics);

                Class<?> c = Class.forName(classFile.getFullyQualifiedName());
                ClassInfo info = c.getAnnotation(ClassInfo.class);

                assertEquals(info.Ce(), g.getMetrics().getIntMetrics("Ce"));
                assertEquals(info.Ca(), g.getMetrics().getIntMetrics("Ca"));
                assertEquals(info.A(), g.getMetrics().getDoubleMetrics("A"));
                assertEquals(info.I(), g.getMetrics().getDoubleMetrics("I"));
                assertEquals(info.Dn(), g.getMetrics().getDoubleMetrics("Dn"));

            }
        }

    }

}