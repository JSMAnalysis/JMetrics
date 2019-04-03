package analysis;

import fr.ubordeaux.jmetrics.analysis.CouplingParser;
import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.analysis.IntrospectionCouplingParser;
import fr.ubordeaux.jmetrics.analysis.JDTCouplingParser;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;
import ground_truth.GroundTruthManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CouplingParserTest {

    private CouplingParser iParser, jParser;
    private GroundTruthManager GT;

    private final static int BYTECODE = 1;
    private final static int SOURCE = 2;

    @BeforeEach
    void setup() {
        GT = new GroundTruthManager();
        iParser = new IntrospectionCouplingParser();
        jParser = new JDTCouplingParser();
    }

    @Test
    void testDependenciesAnalysis() {
        for (int projectNumber = 1; projectNumber <= GroundTruthManager.groundTruthSize; projectNumber++) {
            selectExplorerAndParser(iParser, BYTECODE, projectNumber);
            selectExplorerAndParser(jParser, SOURCE, projectNumber);
        }
    }

    private void selectExplorerAndParser( CouplingParser parser, int explorer, int projectNumber) {
        List<Dependency> GTDependencies = new ArrayList<>();
        if (explorer == BYTECODE) {
            GT.loadExampleBytecode(projectNumber);
            GTDependencies = GT.getProject(projectNumber).getBytecodeDependencies();
        }
        else if (explorer == SOURCE){
            GT.loadExampleSourcecode(projectNumber);
            GTDependencies = GT.getProject(projectNumber).getSourceDependencies();
        }

        List<Dependency> PSDependencies = new ArrayList<>();

        for (ClassFile PSFile: ProjectStructure.getInstance().getClasses()) {
            PSDependencies.addAll(parser.getDependencies(PSFile));
        }

            assertEquals(GTDependencies.size(),PSDependencies.size(),
                    "Project " + projectNumber + " : The number of dependencies analyzed is different from " +
                            "the number of dependencies referenced in the ground truth");

        boolean sameSrc, sameDst, sameType, find;
        for (Dependency GTDependency: GTDependencies) {
            find = false;
            for (Dependency PSDependency: PSDependencies) {
                sameSrc = GTDependency.getSource().getFullyQualifiedName().equals(PSDependency.getSource().getFullyQualifiedName());
                sameDst = GTDependency.getDestination().getFullyQualifiedName().equals(PSDependency.getDestination().getFullyQualifiedName());
                sameType = GTDependency.getType().equals(PSDependency.getType());
                if (find = sameSrc && sameDst && sameType) break;
            }
            if (!find) {
                fail("Project" + projectNumber + ": A dependency in the Ground Truth is not present in the result of analyze.\n" +
                        "Concerned dependency: (" + GTDependency.getType() + "): " +
                        GTDependency.getSource().getFullyQualifiedName() + " -> " + GTDependency.getDestination().getFullyQualifiedName());
            }
        }
    }

}
