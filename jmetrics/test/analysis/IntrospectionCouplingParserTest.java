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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class IntrospectionCouplingParserTest {

    private CouplingParser parser;
    private GroundTruthManager GT;

    @BeforeEach
    void setup() {
        GT = new GroundTruthManager();
        parser = new IntrospectionCouplingParser();
    }

    @Test
    void testDependenciesAnalysis() {
        List<Dependency> GTDependencies, PSDependencies;
        for (int projectNumber = 1; projectNumber <= GroundTruthManager.groundTruthSize; projectNumber++) {

            GTDependencies = GT.getProject(projectNumber).getDependencies();
            GT.loadExample(projectNumber);
            PSDependencies = new ArrayList<>();
            for (ClassFile PSFile: ProjectStructure.getInstance().getClasses()) {
                PSDependencies.addAll(parser.getDependencies(PSFile));
            }

            assertEquals(PSDependencies.size(), GTDependencies.size(),
                    "Project" + projectNumber + " : The number of dependencies analyzed is different from" +
                            "the number of dependencies referenced in the ground truth");

            boolean sameSrc, sameDst, sameType, find;
            for (Dependency GTDependency: GTDependencies) {
                find = false;
                for (Dependency PSDependency: PSDependencies) {
                    sameSrc = GTDependency.getSource().getName().equals(PSDependency.getSource().getName());
                    sameDst = GTDependency.getDestination().getName().equals(PSDependency.getDestination().getName());
                    sameType = GTDependency.getType().equals(PSDependency.getType());
                    if (find = sameSrc && sameDst && sameType) break;
                }
                if (!find) {
                    System.out.println(find);
                    fail("Project" + projectNumber + ": A dependency in the Ground Truth is not present in the result of analyze.\n" +
                            "Concerned dependency: (" + GTDependency.getType() + "): " +
                            GTDependency.getSource() + " -> " + GTDependency.getDestination());
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
