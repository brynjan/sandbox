package no.progconsult;

/**
 * Created by bno on 05.05.15.
 */
public class Calculator {

    public static abstract class Operation {
        private final String name;

        Operation(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        abstract double eval(double x, double y);

        public static final Operation PLUS = new Operation("+") {
            @Override
            double eval(double x, double y) {
                return x + y;
            }
        };
        public static final Operation MINUS = new Operation("+") {
            @Override
            double eval(double x, double y) {
                return x - y;
            }
        };
    }

    public double calculate(Operation op, double x, double y) {
        return op.eval(x, y);
    }
}
