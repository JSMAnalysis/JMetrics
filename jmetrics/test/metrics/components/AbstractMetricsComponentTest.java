package metrics.components;

import fr.ubordeaux.jmetrics.metrics.BadMetricsValueException;
import fr.ubordeaux.jmetrics.metrics.components.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractMetricsComponentTest {

    @Test
    public void testCreateMetricsWithRightValue() {
        MetricsComponent component;

        try {
            component = new Abstractness(0);
            component = new Instability(0.5);
            component = new AfferentCoupling(5);
            component = new EfferentCoupling(0);
            component = new NormalizedDistance(0.563);
        }
        catch (BadMetricsValueException e){
            fail("No exception should be throw when metrics is built with correct values");
        }
    }

    @Test
    public void testCreateMetricsWithWrongValue() {
        MetricsComponent component;

        try {
            component = new Abstractness(1.2);
            fail("Should have thrown");

        }
        catch (BadMetricsValueException e){
            try{
                component = new Instability(-1);
                fail("Should have thrown");

            }
            catch (BadMetricsValueException f){
                try{
                    component = new EfferentCoupling(0.5);
                    fail("Should have thrown");
                }
                catch (BadMetricsValueException g){
                    //pass
                }
            }
        }
    }
}