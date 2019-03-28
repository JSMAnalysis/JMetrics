package metrics;

import fr.ubordeaux.jmetrics.analysis.AbstractnessData;
import fr.ubordeaux.jmetrics.metrics.BadMetricsValueException;
import fr.ubordeaux.jmetrics.metrics.MartinMetrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class MartinMetricsTest {

    private MartinMetrics martinMetrics;
    private Method abstractnessMethod, instabilityMethod, normalizedDistanceMethod;

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
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(0,0));
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(10,0));
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(10,10));
            abstractnessMethod.invoke(martinMetrics, new AbstractnessData(100,63));

            instabilityMethod.invoke(martinMetrics, 0, 0);
            instabilityMethod.invoke(martinMetrics, 0, 10);
            instabilityMethod.invoke(martinMetrics, 10, 0);
            instabilityMethod.invoke(martinMetrics, 100, 63);

            normalizedDistanceMethod.invoke(martinMetrics, 0, 1);
            normalizedDistanceMethod.invoke(martinMetrics, 0, 0);
            normalizedDistanceMethod.invoke(martinMetrics, 1, 0.63);
        } catch (BadMetricsValueException e) {
            fail("No exception should be throw when martinMetrics is built with correct values");
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

}