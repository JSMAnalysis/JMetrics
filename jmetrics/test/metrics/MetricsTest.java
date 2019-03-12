package metrics;

import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.metrics.BadMetricsValueException;
import fr.ubordeaux.jmetrics.metrics.Metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class MetricsTest {

    private Metrics metrics;
    private Method abstractnessMethod, instabilityMethod, normalizedDistanceMethod;

    @BeforeEach
    void setUp() {
        metrics = new Metrics();
        try {
            abstractnessMethod = Metrics.class.getDeclaredMethod("setAbstractness", AbstractnessData.class);
            instabilityMethod = Metrics.class.getDeclaredMethod("setInstability", double.class, double.class);
            normalizedDistanceMethod = Metrics.class.getDeclaredMethod("setNormalizedDistance", double.class, double.class);
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
            abstractnessMethod.invoke(metrics, new AbstractnessData(0,0));
            abstractnessMethod.invoke(metrics, new AbstractnessData(10,0));
            abstractnessMethod.invoke(metrics, new AbstractnessData(10,10));
            abstractnessMethod.invoke(metrics, new AbstractnessData(100,63));

            instabilityMethod.invoke(metrics, 0, 0);
            instabilityMethod.invoke(metrics, 0, 10);
            instabilityMethod.invoke(metrics, 10, 0);
            instabilityMethod.invoke(metrics, 100, 63);

            normalizedDistanceMethod.invoke(metrics, 0, 1);
            normalizedDistanceMethod.invoke(metrics, 0, 0);
            normalizedDistanceMethod.invoke(metrics, 1, 0.63);
        } catch (BadMetricsValueException e){
            fail("No exception should be throw when metrics is built with correct values");
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("(TEST ERROR) Error had been raised while trying to access private method through test: ");
            e.printStackTrace();
        }
    }

    @Test
    void testSetMetricsWrongValue() {
        try {
            abstractnessMethod.invoke(metrics, new AbstractnessData(10, 12));
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }
        try {
            abstractnessMethod.invoke(metrics, new AbstractnessData(-10, 5));
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }

        try {
            instabilityMethod.invoke(metrics, 10, -5);
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }
        try {
            instabilityMethod.invoke(metrics, -5, 10);
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }

        try {
            normalizedDistanceMethod.invoke(metrics, 2, 2);
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }
        try {
            normalizedDistanceMethod.invoke(metrics, -1, -1);
        } catch (IllegalAccessException | InvocationTargetException e) {
            assertEquals(e.getCause().getClass(), BadMetricsValueException.class);
        }
    }

}