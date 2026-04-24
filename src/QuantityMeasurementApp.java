public class QuantityMeasurementApp {

    // ----------- ENUM FOR UNITS -----------
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0);

        private final double toFeet;

        LengthUnit(double toFeet) {
            this.toFeet = toFeet;
        }

        public double toFeet(double value) {
            return value * toFeet;
        }
    }

    // ----------- GENERIC QUANTITY CLASS -----------
    static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        @Override
        public boolean equals(Object obj) {

            // Same reference
            if (this == obj) return true;

            // Null check
            if (obj == null) return false;

            // Type check
            if (getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;

            // Convert both to base unit (feet)
            double thisValue = this.unit.toFeet(this.value);
            double otherValue = other.unit.toFeet(other.value);

            return Double.compare(thisValue, otherValue) == 0;
        }
    }

    // ---------- TEST METHODS ----------

    static void testEquality_FeetToFeet_SameValue() {
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(1.0, LengthUnit.FEET);
        System.out.println("Feet == Feet: " + q1.equals(q2)); // true
    }

    static void testEquality_InchToInch_SameValue() {
        Quantity q1 = new Quantity(1.0, LengthUnit.INCH);
        Quantity q2 = new Quantity(1.0, LengthUnit.INCH);
        System.out.println("Inch == Inch: " + q1.equals(q2)); // true
    }

    static void testEquality_InchToFeet_EquivalentValue() {
        Quantity q1 = new Quantity(12.0, LengthUnit.INCH);
        Quantity q2 = new Quantity(1.0, LengthUnit.FEET);
        System.out.println("12 inch == 1 feet: " + q1.equals(q2)); // true
    }

    static void testEquality_FeetToFeet_DifferentValue() {
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(2.0, LengthUnit.FEET);
        System.out.println("Feet different: " + q1.equals(q2)); // false
    }

    static void testEquality_InchToInch_DifferentValue() {
        Quantity q1 = new Quantity(1.0, LengthUnit.INCH);
        Quantity q2 = new Quantity(2.0, LengthUnit.INCH);
        System.out.println("Inch different: " + q1.equals(q2)); // false
    }

    static void testEquality_NullComparison() {
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        System.out.println("Null comparison: " + q1.equals(null)); // false
    }

    static void testEquality_SameReference() {
        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        System.out.println("Same reference: " + q1.equals(q1)); // true
    }

    static void testEquality_InvalidUnit() {
        try {
            new Quantity(1.0, null);
        } catch (Exception e) {
            System.out.println("Invalid unit handled: " + e.getMessage());
        }
    }

    // ---------- MAIN METHOD ----------
    public static void main(String[] args) {

        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);

        System.out.println("Input: Quantity(1.0, FEET) and Quantity(12.0, INCH)");
        System.out.println("Output: Equal (" + q1.equals(q2) + ")");

        Quantity q3 = new Quantity(1.0, LengthUnit.INCH);
        Quantity q4 = new Quantity(1.0, LengthUnit.INCH);

        System.out.println("Input: Quantity(1.0, INCH) and Quantity(1.0, INCH)");
        System.out.println("Output: Equal (" + q3.equals(q4) + ")");

        System.out.println("\n--- Running Test Cases ---");

        testEquality_FeetToFeet_SameValue();
        testEquality_InchToInch_SameValue();
        testEquality_InchToFeet_EquivalentValue();
        testEquality_FeetToFeet_DifferentValue();
        testEquality_InchToInch_DifferentValue();
        testEquality_NullComparison();
        testEquality_SameReference();
        testEquality_InvalidUnit();
    }
}