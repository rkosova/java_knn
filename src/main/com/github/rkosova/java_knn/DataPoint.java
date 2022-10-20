package com.github.rkosova.java_knn;

import java.util.ArrayList;


/** Represents an instance of a data point.
 * 
 * @author Ron Kosova
 */
public class DataPoint {
    private ArrayList<Double> X = new ArrayList<>();
    private double distance = 0d;

    /**
     * 
     * @param data Array of strings created from splitting one row of training data, class column omitted
     */
    public DataPoint(String data[]) {
        for (int i = 0; i < data.length; i++) { 
            addX(Double.parseDouble(data[i]));
        }
    }

    public ArrayList<Double> getX() {
        return X;
    }

    public void addX(double X) {
        this.X.add(X);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}