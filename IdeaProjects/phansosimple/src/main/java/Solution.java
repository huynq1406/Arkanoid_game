import java.lang.Math;

public class Solution {
        // Type your main code here
        private int numerator;
        private int denominator;

        public void setNumerator(int numerator) {
            this.numerator = numerator;
        }

        public void setDenominator(int denominator) {
            this.denominator = denominator;
        }

        public int getNumerator() {
            return this.numerator;
        }

        public int getDenominator() {
            return this.denominator;
        }

        Solution() {
            this.numerator = 0;
            this.denominator = 1;
        }

        Solution(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public int gcd (int a, int b) {
            if (b == 0) {
                return Math.abs(a);
            }
            return gcd (b, a % b);
        }

        public void reduce() {
            int ucln = gcd(numerator, denominator);
            numerator /= ucln;
            denominator /= ucln;
            if (denominator < 0) {
                denominator = -denominator;
                numerator *= -1;
            }
        }

        public Solution add(Solution other) {
            this.numerator = this.numerator * other.getDenominator() + other.getNumerator() * this.denominator;
            this.denominator = this.denominator * other.getDenominator();
            this.reduce();
            return this;
        }

        public Solution subtract(Solution other) {
            this.numerator = this.numerator * other.getDenominator() - other.getNumerator() * this.denominator;
            this.denominator = this.denominator * other.getDenominator();
            this.reduce();
            return this;
        }

        public Solution multiply(Solution other) {
            this.numerator = this.numerator * other.numerator;
            this.denominator = this.denominator * other.denominator;
            this.reduce();
            return this;
        }

        public Solution divide(Solution other) {
            if (other.denominator == 0) {
                return this;
            }
            this.numerator = this.numerator * other.denominator;
            this.denominator = this.denominator * other.numerator;
            this.reduce();
            return this;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Solution) {
                Solution other = (Solution) obj;

                Solution a = new Solution(this.numerator, this.denominator);
                Solution b = new Solution(other.numerator, other.denominator);

                a.reduce();
                b.reduce();

                return (a.numerator == b.numerator)
                        && (a.denominator == b.denominator);
            }
            return false;
        }
}
