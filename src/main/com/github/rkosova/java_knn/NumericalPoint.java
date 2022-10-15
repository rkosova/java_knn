package com.github.rkosova.java_knn;

import java.util.Arrays; 

public class NumericalPoint extends DataPoint {
    private double known;


    public NumericalPoint(String data[]) {
        super(data);
    }

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
