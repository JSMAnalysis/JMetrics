package analysis;

import fr.ubordeaux.jmetrics.analysis.CouplingParser;
import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.analysis.IntrospectionCouplingParser;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import ground_truth.GroundTruthManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class IntrospectionCouplingParserTest {

    private CouplingParser parser;
    private GroundTruthManager GT;

    @BeforeEach
    void setUp() {
        GT = new GroundTruthManager();
        parser = new IntrospectionCouplingParser();
    }

    @Test
    void testInheritanceDependenciesAnalysis() {
        for (int projectNumber = 1; projectNumber <= GroundTruthManager.groundTruthSize; projectNumber++) {
            GT.loadExample(projectNumber);
            List<Dependency> GTDependencies = GT.getProject(projectNumber).getDependencies();

            List<ClassFile> PSClasses = ProjectStructure.getInstance().getClasses();
            List<Dependency> PSDependencies = new ArrayList<>();
            for (ClassFile PSFile: PSClasses) {
                PSDependencies.addAll(parser.getInheritanceDependencies(PSFile));
                PSDependencies.addAll(parser.getAggregationDependencies(PSFile));
                PSDependencies.addAll(parser.getSignatureDependencies(PSFile));
            }

            printDependencies(PSDependencies);
            System.out.println("\n\n");
            printDependencies(GTDependencies);

            assertEquals(PSDependencies.size(), GTDependencies.size(),
                    "Project" + projectNumber + " : The number of dependencies analyzed is different from" +
                            "the number of dependencies referenced in the ground truth");

            boolean sameSrc, sameDst, sameType;
            Iterator<Dependency> GTiter = GTDependencies.iterator();
            Iterator<Dependency> PSiter = PSDependencies.iterator();
            for (;GTiter.hasNext();) {
                Dependency GTDependency = GTiter.next();
                Dependency PSDependency = PSiter.next();
                sameSrc = GTDependency.getSource().getName().equals(PSDependency.getSource().getName());
                sameDst = GTDependency.getDestination().getName().equals(PSDependency.getDestination().getName());
                sameType = GTDependency.getType().equals(PSDependency.getType());
                if (!sameSrc && !sameDst && !sameType && !GTiter.hasNext()) {
                    fail("Project" + projectNumber + " : A dependency in the Ground Truth is not present in the result of analyze.");
                }
            }
        }
    }

    /**
     * Debug purpose function.
     */
    void printDependencies(List<Dependency> list) {
        int i = 0;
        for (Dependency dependency: list) {
            i++;
            System.out.println(
                   "Dependency " + i + " (" + dependency.getType() + ") : " +
                    dependency.getSource().getName() + " -> " + dependency.getDestination().getName()
            );
        }
    }

}
