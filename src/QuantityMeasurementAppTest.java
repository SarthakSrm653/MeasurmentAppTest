public class QuantityMeasurementAppTest {

    // ===== ENUM =====
    enum LengthUnit {
        FEET(1.0),                  // base unit
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.0328084);

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    // ===== VALUE OBJECT =====
    static final class QuantityLength {

        private final double value;
        private final LengthUnit unit;
        private static final double EPSILON = 1e-6;

        public QuantityLength(double value, LengthUnit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        // Static conversion method (MAIN API)
        public static double convert(double value, LengthUnit source, LengthUnit target) {
            validate(value, source);
            validate(value, target);

            double baseValue = toBase(value, source);
            return fromBase(baseValue, target);
        }

        // Instance method (immutability)
        public QuantityLength convertTo(LengthUnit target) {
            double converted = convert(this.value, this.unit, target);
            return new QuantityLength(converted, target);
        }

        // ===== PRIVATE HELPERS =====
        private static double toBase(double value, LengthUnit unit) {
            return value * unit.getConversionFactor();
        }

        private static double fromBase(double baseValue, LengthUnit unit) {
            return baseValue / unit.getConversionFactor();
        }

        private static void validate(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be finite");
            }
        }

        // ===== OVERRIDES =====
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            double thisBase = toBase(this.value, this.unit);
            double otherBase = toBase(other.value, other.unit);

            return Math.abs(thisBase - otherBase) < EPSILON;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toBase(value, unit));
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // ===== DEMO METHODS (OVERLOADING) =====

    // Method 1: raw values
    public static void demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        double result = QuantityLength.convert(value, from, to);
        System.out.println(value + " " + from + " = " + result + " " + to);
    }

    // Method 2: object input
    public static void demonstrateLengthConversion(QuantityLength length, LengthUnit to) {
        QuantityLength converted = length.convertTo(to);
        System.out.println(length + " = " + converted);
    }

    public static void demonstrateLengthEquality(QuantityLength l1, QuantityLength l2) {
        System.out.println(l1 + " and " + l2 + " equal? " + l1.equals(l2));
    }

    // ===== MAIN METHOD (TESTING) =====
    public static void main(String[] args) {

        // Basic conversions
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);     // 12
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);      // 9
        demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);   // 1

        // Cross-unit
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES);

        // Zero
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES);

        // Negative
        demonstrateLengthConversion(-1.0, LengthUnit.FEET, LengthUnit.INCHES);

        // Instance conversion
        QuantityLength length = new QuantityLength(2.0, LengthUnit.YARDS);
        demonstrateLengthConversion(length, LengthUnit.INCHES);

        // Equality check
        QuantityLength l1 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength l2 = new QuantityLength(1.0, LengthUnit.FEET);
        demonstrateLengthEquality(l1, l2);

        // Round-trip test
        double value = 5.0;
        double converted = QuantityLength.convert(value, LengthUnit.FEET, LengthUnit.INCHES);
        double back = QuantityLength.convert(converted, LengthUnit.INCHES, LengthUnit.FEET);
        System.out.println("Round-trip preserved? " + (Math.abs(value - back) < 1e-6));

        // Exception test
        try {
            QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
        } catch (Exception e) {
            System.out.println("Handled invalid input: " + e.getMessage());
        }
    }
}