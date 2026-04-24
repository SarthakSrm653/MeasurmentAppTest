import java.util.Objects;

/**
 * 1. ENUM: LengthUnit
 * Defines conversion factors relative to FEET (the base unit).
 */
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

/**
 * 2. CORE CLASS: QuantityLength
 * Handles immutability, conversion logic, and equality.
 */
class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        validateInput(value, unit);
        this.value = value;
        this.unit = unit;
    }

    private void validateInput(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number (not NaN or Infinity)");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
    }

    /**
     * Static Method: Unit-to-Unit Conversion
     * Normalizes source to base unit, then converts to target.
     */
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null) throw new IllegalArgumentException("Units cannot be null");
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");

        double valueInBase = value * source.getConversionFactor();
        return valueInBase / target.getConversionFactor();
    }

    /**
     * Instance Method: Convert existing object to new unit
     */
    public QuantityLength convertTo(LengthUnit targetUnit) {
        double convertedValue = convert(this.value, this.unit, targetUnit);
        return new QuantityLength(convertedValue, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityLength that = (QuantityLength) o;

        // Compare based on base unit normalization with 1e-6 epsilon for precision
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
        return String.format("%.2f %s", value, unit);
    }
}

/**
 * 3. APP CLASS: QuantityMeasurementApp
 * Demonstrates API usage and Method Overloading.
 */
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        System.out.println("=== UC5: Unit-to-Unit Conversion Demo ===\n");

        // Demonstration of Static Method API
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);
        demonstrateLengthConversion(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES);

        System.out.println("\n=== Demonstration of Method Overloading & Equality ===");

        // Creating an instance
        QuantityLength threeFeet = new QuantityLength(3.0, LengthUnit.FEET);

        // Using overloaded method (QuantityLength object vs raw double)
        demonstrateLengthConversion(threeFeet, LengthUnit.YARDS);

        // Equality Check Demo
        QuantityLength oneYard = new QuantityLength(1.0, LengthUnit.YARDS);
        demonstrateLengthEquality(threeFeet, oneYard);
    }

    /**
     * Overloaded Method 1: Takes raw numeric values
     */
    public static void demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        double result = QuantityLength.convert(value, from, to);
        System.out.printf("Input: %.1f %s -> Converted: %.4f %s%n", value, from, result, to);
    }

    /**
     * Overloaded Method 2: Takes a QuantityLength object
     */
    public static void demonstrateLengthConversion(QuantityLength length, LengthUnit toUnit) {
        QuantityLength converted = length.convertTo(toUnit);
        System.out.println("Object Input: " + length + " -> Converted Object: " + converted);
    }

    public static void demonstrateLengthEquality(QuantityLength l1, QuantityLength l2) {
        boolean isEqual = l1.equals(l2);
        System.out.println("Comparing [" + l1 + "] and [" + l2 + "] -> Equal? " + isEqual);
    }
}