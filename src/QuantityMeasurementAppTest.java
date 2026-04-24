import java.util.Objects;

/**
 * UC4: Extended Unit Support
 * This implementation adds YARDS and CENTIMETERS to the existing FEET and INCHES logic.
 * All units are compared by converting to a common base (INCHES).
 */

// 1. Enum with all length units and their factors relative to INCHES
enum LengthUnit {
    FEET(12.0),           // 1 foot = 12 inches
    INCHES(1.0),          // Base unit
    YARDS(36.0),          // 1 yard = 3 feet = 36 inches
    CENTIMETERS(0.393701); // 1 cm = 0.393701 inches

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }
}

// 2. QuantityLength Class
class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityLength that = (QuantityLength) o;

        // Convert both values to the base unit (INCHES) for comparison
        double value1InInches = this.value * this.unit.getConversionFactor();
        double value2InInches = that.value * that.unit.getConversionFactor();

        // Use an epsilon (1e-6) to handle floating-point precision differences
        return Math.abs(value1InInches - value2InInches) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value * unit.getConversionFactor());
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}

// 3. Main App to demonstrate UC4 requirements
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("=== UC4: Extended Unit Support Demo ===\n");

        // Yard to Feet: 1 yd = 3 ft
        compare(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(3.0, LengthUnit.FEET));

        // Yard to Inches: 1 yd = 36 in
        compare(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(36.0, LengthUnit.INCHES));

        // Centimeters to Inches: 1 cm = 0.393701 in
        compare(new QuantityLength(1.0, LengthUnit.CENTIMETERS), new QuantityLength(0.393701, LengthUnit.INCHES));

        // Complex Multi-Unit: 2 yards = 72 inches
        compare(new QuantityLength(2.0, LengthUnit.YARDS), new QuantityLength(72.0, LengthUnit.INCHES));

        // Centimeters to Feet
        compare(new QuantityLength(30.48, LengthUnit.CENTIMETERS), new QuantityLength(1.0, LengthUnit.FEET));
    }

    private static void compare(QuantityLength l1, QuantityLength l2) {
        System.out.println("Comparing: " + l1 + " and " + l2);
        System.out.println("Result: " + (l1.equals(l2) ? "Equal" : "Not Equal") + "\n");
    }
}