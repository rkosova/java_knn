package com.github.rkosova.java_knn;

public class NumericalPoint extends DataPoint {
    private double known;

    public NumericalPoint(String data[], int classColumn) {
        super(data);
        setKnown(Double.parseDouble(data[classColumn - 1]));
    }

    public void setKnown(double known) {
        this.known = known;
    }

    public double getKnown() {
        return known;
    }
}
