import java.util.Objects;

/**
 * UC8: Refactored Unit Conversion API
 * 1. LengthUnit: Standalone Enum with Conversion Responsibility.
 * 2. QuantityLength: Domain logic for Comparison and Arithmetic.
 * 3. QuantityMeasurementApp: Client/Demo code.
 */

// --- 1. STANDALONE ENUM (Conversion Responsibility) ---
enum LengthUnit {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    /**
     * Responsibility: Convert a value in THIS unit to the base unit (FEET).
     */
    public double convertToBaseUnit(double value) {
        return value * this.conversionFactor;
    }

    /**
     * Responsibility: Convert a value FROM the base unit (FEET) to THIS unit.
     */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }
}

// --- 2. REFACTORED QUANTITY CLASS (Delegation Pattern) ---
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
     * UC5 Refactored: Delegates conversion to the unit class.
     */
    public QuantityLength convertTo(LengthUnit targetUnit) {
        Objects.requireNonNull(targetUnit);
        double baseValue = this.unit.convertToBaseUnit(this.value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
        return new QuantityLength(convertedValue, targetUnit);
    }

    /**
     * UC7 Refactored: Addition with explicit target unit specification.
     */
    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(targetUnit);

        // Normalize both to base unit using the enum's responsibility
        double baseVal1 = this.unit.convertToBaseUnit(this.value);
        double baseVal2 = other.unit.convertToBaseUnit(other.value);

        // Perform arithmetic and convert back via targetUnit
        double sumInBase = baseVal1 + baseVal2;
        double finalValue = targetUnit.convertFromBaseUnit(sumInBase);

        return new QuantityLength(finalValue, targetUnit);
    }

    /**
     * UC6 Backward Compatibility: Default addition to first operand's unit.
     */
    public QuantityLength add(QuantityLength other) {
        return this.add(other, this.unit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityLength that = (QuantityLength) o;

        // Use enum responsibility for normalization
        double v1 = this.unit.convertToBaseUnit(this.value);
        double v2 = that.unit.convertToBaseUnit(that.value);
        return Math.abs(v1 - v2) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.convertToBaseUnit(value));
    }

    @Override
    public String toString() {
        return String.format("Quantity(%.3f, %s)", value, unit);
    }
}

// --- 3. MAIN APP (Verification of UC8) ---
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("=== UC8: Refactored Design Demo ===\n");

        // Verification: Conversion delegation
        QuantityLength oneFoot = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println("1ft to Inches: " + oneFoot.convertTo(LengthUnit.INCHES));

        // Verification: Addition delegation
        QuantityLength twelveInches = new QuantityLength(12.0, LengthUnit.INCHES);
        System.out.println("1ft + 12in (Target Yards): " + oneFoot.add(twelveInches, LengthUnit.YARDS));

        // Verification: Equality delegation
        QuantityLength thirtySixInches = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength oneYard = new QuantityLength(1.0, LengthUnit.YARDS);
        System.out.println("36in == 1yd? " + thirtySixInches.equals(oneYard));

        // Verification: Direct Enum Responsibility (SRP Test)
        System.out.println("Enum logic (12in to Feet): " + LengthUnit.INCHES.convertToBaseUnit(12.0));
    }
}