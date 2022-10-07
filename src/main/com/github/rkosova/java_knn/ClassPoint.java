package com.github.rkosova.java_knn;

public class ClassPoint extends DataPoint {
    private String classification;

    public ClassPoint(String data[], int classColumn) {
        super(data);
        setClassification(data[classColumn - 1]);
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

     public String getClassification() {
        return classification;
    }
    
}
