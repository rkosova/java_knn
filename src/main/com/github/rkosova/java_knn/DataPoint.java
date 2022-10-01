package com.github.rkosova.java_knn;

import java.util.ArrayList;

public class DataPoint {
    private ArrayList<Double> X;

    public DataPoint(String data[]) {
        for (int i = 0; i < data.length - 1; i++) {
            addX(Double.parseDouble(data[i]));
        }
    }

    public ArrayList<Double> getX() {
        return X;
    }

    public void addX(double X) {
        this.X.add(X);
    }
}