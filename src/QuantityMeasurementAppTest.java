import java.util.Objects;

/**
 * UC7: Addition with Target Unit Specification
 * This API allows adding two length measurements and explicitly
 * defining the unit in which the result should be returned.
 */

// 1. LengthUnit Enum (UC5 - UC7)
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

// 2. Core QuantityLength Class
class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number (not NaN/Infinity)");
        }
        this.value = value;
        this.unit = Objects.requireNonNull(unit, "Unit cannot be null");
    }

    public double getValue() { return value; }
    public LengthUnit getUnit() { return unit; }

    /**
     * PRIVATE UTILITY METHOD (UC7 Concept)
     * Centralizes addition logic to maintain the DRY principle.
     * Converts inputs to base (FEET), sums them, and converts to target.
     */
    private static QuantityLength performAddition(QuantityLength l1, QuantityLength l2, LengthUnit target) {
        Objects.requireNonNull(l1, "First operand cannot be null");
        Objects.requireNonNull(l2, "Second operand cannot be null");
        Objects.requireNonNull(target, "Target unit cannot be null");

        // Normalize to base (FEET)
        double baseVal1 = l1.value * l1.unit.getConversionFactor();
        double baseVal2 = l2.value * l2.unit.getConversionFactor();

        // Sum and convert to target unit
        double sumInBase = baseVal1 + baseVal2;
        double resultValue = sumInBase / target.getConversionFactor();

        return new QuantityLength(resultValue, target);
    }

    /**
     * UC6: Addition defaulting to the unit of the first operand.
     * (Leverages the private utility method)
     */
    public QuantityLength add(QuantityLength other) {
        return performAddition(this, other, this.unit);
    }

    /**
     * UC7: Overloaded Static Method for Explicit Target Unit Specification.
     * Allows the caller to define the resulting unit.
     */
    public static QuantityLength add(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {
        return performAddition(l1, l2, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityLength that = (QuantityLength) o;

        // Epsilon-based equality comparison at the base unit level
        double v1 = this.value * this.unit.getConversionFactor();
        double v2 = that.value * that.unit.getConversionFactor();
        return Math.abs(v1 - v2) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value * unit.getConversionFactor());
    }

    @Override
    public String toString() {
        return String.format("Quantity(%.3f, %s)", value, unit);
    }
}

// 3. Application Class for UC7 Testing/Demo
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        System.out.println("=== UC7: Addition with Target Unit Specification ===\n");

        QuantityLength oneFoot = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength twelveInches = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength oneYard = new QuantityLength(1.0, LengthUnit.YARDS);

        // 1. Target: FEET (1ft + 12in = 2ft)
        printResult(oneFoot, twelveInches, LengthUnit.FEET);

        // 2. Target: INCHES (1ft + 12in = 24in)
        printResult(oneFoot, twelveInches, LengthUnit.INCHES);

        // 3. Target: YARDS (1ft + 12in = ~0.667 yards)
        printResult(oneFoot, twelveInches, LengthUnit.YARDS);

        // 4. Target: FEET (36in + 1yd = 6ft)
        QuantityLength thirtySixInches = new QuantityLength(36.0, LengthUnit.INCHES);
        printResult(thirtySixInches, oneYard, LengthUnit.FEET);

        // 5. Target: CENTIMETERS (2.54cm + 1in = 5.08cm)
        QuantityLength cmVal = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength oneInch = new QuantityLength(1.0, LengthUnit.INCHES);
        printResult(cmVal, oneInch, LengthUnit.CENTIMETERS);

        // 6. Identity & Target Change (5ft + 0in = ~1.667 yards)
        QuantityLength zeroInches = new QuantityLength(0.0, LengthUnit.INCHES);
        printResult(new QuantityLength(5.0, LengthUnit.FEET), zeroInches, LengthUnit.YARDS);

        // 7. Negative Values & Target: INCHES (5ft + -2ft = 36in)
        QuantityLength negTwoFeet = new QuantityLength(-2.0, LengthUnit.FEET);
        printResult(new QuantityLength(5.0, LengthUnit.FEET), negTwoFeet, LengthUnit.INCHES);
    }

    /**
     * Helper method to format output for the console.
     */
    private static void printResult(QuantityLength l1, QuantityLength l2, LengthUnit target) {
        QuantityLength result = QuantityLength.add(l1, l2, target);
        System.out.printf("Add: [%s] + [%s] -> Target: %s%n", l1, l2, target);
        System.out.printf("Result: %s%n%n", result);
    }
}