package analysis;

import fr.ubordeaux.jmetrics.analysis.IntrospectionCouplingParser;
import fr.ubordeaux.jmetrics.analysis.IntrospectionParser;

import ground_truth.GroundTruthManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntrospectionCouplingParserTest {

    private IntrospectionParser parser;
    private GroundTruthManager GT;

    @BeforeEach
    void setUp() {
        GT = new GroundTruthManager();
        parser = new IntrospectionCouplingParser();
    }

    @Test
    void testInheritanceDependenciesAnalysis() {

    }

    @Test
    void testAggregationDependenciesAnalysis() {

    }

    @Test
    void testSignatureDependenciesAnalysis() {

    }

}
