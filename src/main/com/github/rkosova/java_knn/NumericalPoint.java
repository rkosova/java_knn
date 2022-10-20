package com.github.rkosova.java_knn;

import java.util.Arrays; 

/** Represents an instance of a numerical data point.
 * 
 * @author Ron Kosova
 */
public class NumericalPoint extends DataPoint {
    private double known;


    /**
     * @param data  Number of the class column for the training data
     */
    public NumericalPoint(String data[]) {
        super(data);
    }

    /**
     * @param data  Number of the class column for the training data
     * @param classColumn Number of the class column
     */
    public NumericalPoint(String data[], int classColumn) {
        super(Arrays.copyOfRange(data, 0, data.length - 1));
        setKnown(Double.parseDouble(data[classColumn - 1]));
    }

    public void setKnown(double known) {
        this.known = known;
    }

    public double getKnown() {
        return known;
    }
}
