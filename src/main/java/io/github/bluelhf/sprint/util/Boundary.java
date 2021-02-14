package io.github.bluelhf.sprint.util;

/**
 * Represents a one-dimensional boundary
 */
@SuppressWarnings("unused")
public class Boundary {

    public static final Boundary ZERO_ONE = new Boundary(0, 1);

    private double low = 0;
    private double high = 1;

    /**
     * @param lower The lower bound of the Boundary
     * @param upper The upper bound of the Boundary
     */
    public Boundary(double lower, double upper) {
        this.low = lower;
        this.high = upper;
    }

    /**
     * Represents a Boundary between 0 and 1
     */
    public Boundary() {
    }

    /**
     * Represents a Boundary with a lower bound of 0.
     *
     * @param upper The upper bound of the Boundary
     */
    public Boundary(double upper) {
        this.high = upper;
    }

    /**
     * @return The lower bound of the Boundary
     */
    public double getLower() {
        return low;
    }

    /**
     * Sets the lower bound of the Boundary
     *
     * @return Itself with the new lower bound
     */
    public Boundary setLower(double low) {
        this.low = low;
        return this;
    }

    /**
     * @return The upper bound of the Boundary
     */
    public double getUpper() {
        return high;
    }

    /**
     * Sets the upper bound of the Boundary
     *
     * @return Itself with the new lower bound
     */
    public Boundary setUpper(double upper) {
        this.high = upper;
        return this;
    }

    /**
     * Constrains a number between the lower and upper bounds of this Boundary (inclusive)
     *
     * @param val The value to constrain
     * @return The constrained value
     */
    public double constrain(double val) {
        return Math.max(low, Math.min(high, val));
    }

    /**
     * Maps a number from a given range to the lower and upper bounds of the Boundary<br/>
     * If val is 0.5, lower is 0, and upper is 1, and the lower and upper bounds of this Boundary are 0 and 2, the output is 1.
     *
     * @param val   The value to map
     * @param lower The value's old lower bound
     * @param upper The value's old upper bound
     * @return The mapped value
     */
    public double map(double val, double lower, double upper) {
        return (val - lower) * (high - low) / (upper - lower) + low;
    }
}