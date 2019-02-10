package analysis;

import fr.ubordeaux.jmetrics.analysis.AbstractnessParser;
import fr.ubordeaux.jmetrics.analysis.IntrospectionAbstractnessParser;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import ground_truth.GroundTruthManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

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
        GT.loadExample(1);
        ProjectStructure PS = ProjectStructure.getInstance();
        ClassFile file = PS.getClassFile("Airplane.class");
        if (file == null) fail("Error");
        int numberOfMethods = parser.getNumberOfMethods(file);
    }

    @Test
    void countAbstractMethodTest() {

    }

}
