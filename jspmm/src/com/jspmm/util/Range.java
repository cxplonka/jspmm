/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cplonka
 */
public class Range {

    private double lower;
    private double upper;

    public Range() {
        this.lower = 0;
        this.upper = 0;
    }

    public Range(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public Range(float... values) {
        lower = Float.MAX_VALUE;
        upper = -Float.MAX_VALUE;
        /* create range from values */
        add(values);
    }

    public Range(double... values) {
        lower = Float.MAX_VALUE;
        upper = -Float.MAX_VALUE;
        /* create range from values */
        add(values);
    }

    public Range(Range range) {
        this(range.getLowerBound(), range.getUpperBound());
    }

    public double getUpperBound() {
        return upper;
    }

    public void setUpperBound(double upper) {
        this.upper = upper;
    }

    public double getLowerBound() {
        return lower;
    }

    public void setLowerBound(double lower) {
        this.lower = lower;
    }

    public void set(Range range) {
        this.lower = range.getLowerBound();
        this.upper = range.getUpperBound();
    }

    public void setBounds(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * transform the fraction back to the range bounds
     *
     * @param fraction normalized value [0..1]
     * @return
     */
    public double invert(double fraction) {
        return lower + fraction * (upper - lower);
    }

    public boolean inside(double value) {
        return value >= lower && value <= upper;
    }

    /**
     * clamp the value to the range
     *
     * @param value
     * @return
     */
    public double clamp(double value) {
        return value < lower ? lower : value > upper ? upper : value;
    }

    /**
     * normalize the value between the upper and lower range [0..1]
     *
     * @param value
     * @return
     */
    public double normalize(double value) {
        return (value - lower) / (upper - lower);
    }

    public void add(float... values) {
        for (float value : values) {
            add(value);
        }
    }

    public void add(double... values) {
        for (double value : values) {
            add(value);
        }
    }

    /**
     * if the value is greater or lower then the current bounds, then we take
     * the value
     *
     * @param value
     * @return
     */
    public void add(double value) {
        if (value < lower) {
            lower = value;
        }
        /* so we have valid bounds when we initilize with invalid values */
        if (value > upper) {
            upper = value;
        }
    }

    public void add(Range range) {
        add(range.getLowerBound());
        add(range.getUpperBound());
    }

    public double getWidth() {
        return (upper - lower);
    }

    public double[] toDouble() {
        return new double[]{lower, upper};
    }

    /**
     * transform the normalized values back to the range values
     *
     * @param fractions normalized values[0..1]
     * @return
     */
    public double[] toValues(float[] fractions) {
        double[] ret = new double[fractions.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = invert(fractions[i]);
        }
        return ret;
    }

    public static Range[] subRange(int size, int steps) {
        List<Range> ret = new ArrayList<Range>(steps);
        if (size < steps || steps == 1) {
            ret.add(new Range(0, size));
        } else {
            for (int i = 0, dx = size / steps; i < size; i += dx) {
                int j = (i + dx) % size;
                if (j - i < 0) {
                    j = i + dx - j;
                }
                ret.add(new Range(i, j));
            }
        }
        return ret.toArray(new Range[ret.size()]);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Range other = (Range) obj;
        if (this.lower != other.lower) {
            return false;
        }
        return this.upper == other.upper;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.lower)
                ^ (Double.doubleToLongBits(this.lower) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.upper)
                ^ (Double.doubleToLongBits(this.upper) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return String.format("lower: %s upper: %s width: %s", lower, upper, getWidth());
    }

    public static float min(float... values) {
        float min = values[0];
        for (float value : values) {
            min = Math.min(value, min);
        }
        return min;
    }

    public static float max(float... values) {
        float min = values[0];
        for (float value : values) {
            min = Math.max(value, min);
        }
        return min;
    }
}
