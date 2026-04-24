import java.util.Objects;

/**
 * 1. UNIT ENUMS (Responsibility: Conversion Logic)
 * Each category (Length, Weight) has its own standalone enum to prevent circular dependencies.
 */

enum LengthUnit {
    FEET(1.0), INCHES(1.0 / 12.0), YARDS(3.0), CENTIMETERS(1.0 / 30.48);

    private final double factor;
    LengthUnit(double factor) { this.factor = factor; }
    public double toBase(double val) { return val * this.factor; }
    public double fromBase(double baseVal) { return baseVal / this.factor; }
}

enum WeightUnit {
    KILOGRAM(1.0), GRAM(0.001), POUND(0.453592);

    private final double factor;
    WeightUnit(double factor) { this.factor = factor; }
    public double toBase(double val) { return val * this.factor; }
    public double fromBase(double baseVal) { return baseVal / this.factor; }
}

/**
 * 2. QUANTITY LENGTH (Category: Length)
 */
class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
        this.value = value;
        this.unit = Objects.requireNonNull(unit);
    }

    public QuantityLength convertTo(LengthUnit target) {
        return new QuantityLength(target.fromBase(this.unit.toBase(this.value)), target);
    }

    public QuantityLength add(QuantityLength other, LengthUnit target) {
        double sumBase = this.unit.toBase(this.value) + other.unit.toBase(other.value);
        return new QuantityLength(target.fromBase(sumBase), target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityLength that = (QuantityLength) o;
        return Math.abs(this.unit.toBase(this.value) - that.unit.toBase(that.value)) < 1e-6;
    }

    @Override
    public String toString() { return String.format("%.3f %s", value, unit); }
}

/**
 * 3. QUANTITY WEIGHT (Category: Weight)
 */
class QuantityWeight {
    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
        this.value = value;
        this.unit = Objects.requireNonNull(unit);
    }

    public QuantityWeight convertTo(WeightUnit target) {
        return new QuantityWeight(target.fromBase(this.unit.toBase(this.value)), target);
    }

    public QuantityWeight add(QuantityWeight other, WeightUnit target) {
        double sumBase = this.unit.toBase(this.value) + other.unit.toBase(other.value);
        return new QuantityWeight(target.fromBase(sumBase), target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityWeight that = (QuantityWeight) o;
        return Math.abs(this.unit.toBase(this.value) - that.unit.toBase(that.value)) < 1e-6;
    }

    @Override
    public String toString() { return String.format("%.3f %s", value, unit); }
}

/**
 * 4. MAIN APPLICATION
 */
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("=== UC9: Multi-Category Measurement System ===\n");

        // --- LENGTH DEMO ---
        QuantityLength oneYard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength threeFeet = new QuantityLength(3.0, LengthUnit.FEET);
        System.out.println("Length Equality (1yd == 3ft): " + oneYard.equals(threeFeet));

        // --- WEIGHT DEMO ---
        QuantityWeight oneKg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight grams = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight pounds = new QuantityWeight(2.20462, WeightUnit.POUND);

        System.out.println("Weight Equality (1kg == 1000g): " + oneKg.equals(grams));
        System.out.println("Weight Equality (1kg == 2.20462lb): " + oneKg.equals(pounds));

        // --- ADDITION DEMO ---
        QuantityWeight sumWeight = oneKg.add(new QuantityWeight(1.0, WeightUnit.POUND), WeightUnit.GRAM);
        System.out.println("Addition (1kg + 1lb) in Grams: " + sumWeight);

        // --- CATEGORY TYPE SAFETY DEMO ---
        System.out.println("\nCategory Incompatibility Check:");
        // Comparing Weight to Length should return false, not true or an error
        System.out.println("Does 1kg equal 1ft? " + oneKg.equals(threeFeet));
    }
}