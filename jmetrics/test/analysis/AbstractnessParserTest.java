package analysis;

import fr.ubordeaux.jmetrics.analysis.AbstractnessData;
import fr.ubordeaux.jmetrics.analysis.AbstractnessParser;
import fr.ubordeaux.jmetrics.analysis.IntrospectionAbstractnessParser;
import fr.ubordeaux.jmetrics.analysis.JDTAbstractnessParser;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;
import ground_truth.ClassInformation;
import ground_truth.GroundTruthManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractnessParserTest {

    private AbstractnessParser iParser, jParser;
    private GroundTruthManager GT;

    private final static int BYTECODE = 1;
    private final static int SOURCE = 2;

    @BeforeEach
    void setup() {
        GT = new GroundTruthManager();
        iParser = new IntrospectionAbstractnessParser();
        jParser = new JDTAbstractnessParser();
    }

    @Test
    void countMethodTest() {
        for (int projectNumber = 1; projectNumber <= GroundTruthManager.groundTruthSize; projectNumber++) {
            selectExplorerForCountingMethod(iParser, BYTECODE, projectNumber);
            selectExplorerForCountingMethod(jParser, SOURCE, projectNumber);
        }
    }

    private void selectExplorerForCountingMethod(AbstractnessParser parser, int explorer, int projectNumber) {
        if (explorer == BYTECODE) GT.loadExampleBytecode(projectNumber);
        else if (explorer == SOURCE) GT.loadExampleSourcecode(projectNumber);

        List<ClassFile> PSClasses = ProjectStructure.getInstance().getClasses();
        Map<ClassFile, ClassInformation> GTClasses = GT.getProject(projectNumber).getClasses();
        for (ClassFile PSFile: PSClasses) {
            AbstractnessData data = parser.getAbstractnessData(PSFile);
            int numberOfMethodsCalculated = data.getNumberOfMethods();
            int numberOfAbstractMethodsCalculated = data.getNumberOfAbstractMethods();

            for (ClassFile GTFile: GTClasses.keySet()) {
                if (PSFile.getFullyQualifiedName().equals(GTFile.getFullyQualifiedName())) {
                    ClassInformation GTInfo = GTClasses.get(GTFile);
                    assertEquals(numberOfMethodsCalculated, GTInfo.getNumberOfMethod(),
                            "Fail nbMethod Project" + projectNumber + " for class " +
                                    GTFile.getFullyQualifiedName());
                    assertEquals(numberOfAbstractMethodsCalculated, GTInfo.getNumberOfAbstractMethod(),
                            "Fail nbAbstractMethod Project" + projectNumber  +
                                    " for class " + GTFile.getFullyQualifiedName());
                }
            }
        }

    }

}
