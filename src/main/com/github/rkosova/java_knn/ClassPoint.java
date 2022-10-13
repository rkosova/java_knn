package com.github.rkosova.java_knn;

import java.util.Arrays;

public class ClassPoint extends DataPoint {
    private String classification;

    public ClassPoint(String data[]) { // called when initializing unclassified ClassPoints
        super(data);
        setClassification(null);
    }

    public ClassPoint(String data[], int classColumn) { // called when initializing classified ClassPoints
        super(Arrays.copyOfRange(data, 0, data.length - 1)); // passes copy of array without class 
        setClassification(data[classColumn - 1]);
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }
    
}
