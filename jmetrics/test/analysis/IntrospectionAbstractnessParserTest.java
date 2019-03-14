package analysis;

import fr.ubordeaux.jmetrics.analysis.AbstractnessParser;
import fr.ubordeaux.jmetrics.analysis.IntrospectionAbstractnessParser;
import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import ground_truth.ClassInformation;
import ground_truth.GroundTruthManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntrospectionAbstractnessParserTest {

    private AbstractnessParser parser;
    private GroundTruthManager GT;

    @BeforeEach
    void setup() {
        GT = new GroundTruthManager();
        parser = new IntrospectionAbstractnessParser();
    }

    @Test
    void countMethodTest() {
        for (int projectNumber = 1; projectNumber <= GroundTruthManager.groundTruthSize; projectNumber++) {
            GT.loadExampleBytecode(projectNumber);
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
                                "Fail nbMethod Project" + projectNumber);
                        assertEquals(numberOfAbstractMethodsCalculated, GTInfo.getNumberOfAbstractMethod(),
                                "Fail nbAbstractMethod Project" + projectNumber);
                    }
                }
            }
        }
    }

}
