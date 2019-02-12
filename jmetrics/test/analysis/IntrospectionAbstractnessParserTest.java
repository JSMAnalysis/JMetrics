package analysis;

import fr.ubordeaux.jmetrics.analysis.AbstractnessParser;
import fr.ubordeaux.jmetrics.analysis.IntrospectionAbstractnessParser;
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
    void setUp() {
        GT = new GroundTruthManager();
        parser = new IntrospectionAbstractnessParser();
    }

    @Test
    void countMethodTest() {
        for (int projectNumber = 1; projectNumber <= GroundTruthManager.groundTruthSize; projectNumber++) {
            GT.loadExample(projectNumber);
            List<ClassFile> PSClasses = ProjectStructure.getInstance().getClasses();
            Map<ClassFile, ClassInformation> GTClasses = GT.getProject(projectNumber).getClasses();
            for (ClassFile PSFile: PSClasses) {
                int numberOfMethodsCalculated = parser.getNumberOfMethods(PSFile);
                int numberOfAbstractMethodsCalculated = parser.getNumberOfAbstractMethods(PSFile);

                for (ClassFile GTFile: GTClasses.keySet()) {
                    if (PSFile.getName().equals(GTFile.getName())) {
                        ClassInformation GTInfo = GTClasses.get(GTFile);
                        assertEquals(numberOfMethodsCalculated, GTInfo.getNumberOfMethod());
                        assertEquals(numberOfAbstractMethodsCalculated, GTInfo.getNumberOfAbstractMethod());
                    }
                }
            }
        }
    }

}
