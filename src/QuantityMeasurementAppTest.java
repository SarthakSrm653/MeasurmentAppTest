import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAdditionTest {

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength expected = new QuantityLength(3.0, LengthUnit.FEET);
        assertEquals(expected, l1.add(l2));
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(12.0, LengthUnit.INCHES);
        // 1ft + 12in = 2ft
        QuantityLength expected = new QuantityLength(2.0, LengthUnit.FEET);
        assertEquals(expected, l1.add(l2));
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLength l1 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength l2 = new QuantityLength(1.0, LengthUnit.FEET);
        // 12in + 1ft = 24in (Result unit follows first operand)
        QuantityLength expected = new QuantityLength(24.0, LengthUnit.INCHES);
        assertEquals(expected, l1.add(l2));
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength l2 = new QuantityLength(3.0, LengthUnit.FEET);
        // 1yd + 3ft = 2yd
        QuantityLength expected = new QuantityLength(2.0, LengthUnit.YARDS);
        assertEquals(expected, l1.add(l2));
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        QuantityLength l1 = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength l2 = new QuantityLength(1.0, LengthUnit.INCHES);
        // 2.54cm + 1in (2.54cm) = 5.08cm
        QuantityLength result = l1.add(l2);
        assertEquals(5.08, result.getValue(), 1e-6);
        assertEquals(LengthUnit.CENTIMETERS, result.getUnit());
    }
}