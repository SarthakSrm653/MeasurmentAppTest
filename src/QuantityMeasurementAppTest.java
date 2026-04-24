import java.util.Objects;

/**
 * UC6: Addition of Two Length Units (Same Category)
 * This implementation allows adding different units (Feet, Inches, Yards, CM)
 * and returns the result in the unit of the first operand.
 */

// 1. Enum with Conversion Factors relative to FEET
enum LengthUnit {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }
}

// 2. QuantityLength Class (Immutable Value Object)
class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        this.value = value;
        this.unit = Objects.requireNonNull(unit, "Unit cannot be null");
    }

    public double getValue() { return value; }
    public LengthUnit getUnit() { return unit; }

    /**
     * UC6 Logic: Addition of two lengths.
     * Converts both to a common base (Feet), sums them,
     * and returns a new object in the first operand's unit.
     */
    public QuantityLength add(QuantityLength other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");

        // Step 1: Convert both to common base (Feet)
        double baseVal1 = this.value * this.unit.getConversionFactor();
        double baseVal2 = other.value * other.unit.getConversionFactor();

        // Step 2: Add values
        double totalBase = baseVal1 + baseVal2;

        // Step 3: Convert from base back to the unit of the first operand
        double resultInTargetUnit = totalBase / this.unit.getConversionFactor();

        return new QuantityLength(resultInTargetUnit, this.unit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityLength that = (QuantityLength) o;

        // Normalize both for equality comparison using epsilon tolerance
        double v1 = this.value * this.unit.getConversionFactor();
        double v2 = that.value * that.unit.getConversionFactor();
        return Math.abs(v1 - v2) < 1e-6;
    }

    @Override
    public String toString() {
        return String.format("Quantity(%.2f, %s)", value, unit);
    }
}

// 3. Main Application for Standalone Testing
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        System.out.println("=== UC6: Addition of Length Units Demo ===\n");

        // 1. Same Unit Addition: 1ft + 2ft = 3ft
        performAddition(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(2.0, LengthUnit.FEET));

        // 2. Cross-Unit Addition: 1ft + 12in = 2ft
        performAddition(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES));

        // 3. Reverse Cross-Unit: 12in + 1ft = 24in
        performAddition(new QuantityLength(12.0, LengthUnit.INCHES),
                new QuantityLength(1.0, LengthUnit.FEET));

        // 4. Yards and Feet: 1yd + 3ft = 2yd
        performAddition(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET));

        // 5. CM and Inches: 2.54cm + 1in = 5.08cm
        performAddition(new QuantityLength(2.54, LengthUnit.CENTIMETERS),
                new QuantityLength(1.0, LengthUnit.INCHES));

        // 6. Identity (Zero): 5ft + 0in = 5ft
        performAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(0.0, LengthUnit.INCHES));

        // 7. Negative values: 5ft + (-2ft) = 3ft
        performAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(-2.0, LengthUnit.FEET));
    }

    private static void performAddition(QuantityLength l1, QuantityLength l2) {
        QuantityLength sum = l1.add(l2);
        System.out.printf("Input: %s + %s%n", l1, l2);
        System.out.printf("Output: %s%n%n", sum);
    }
}