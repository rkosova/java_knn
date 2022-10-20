package com.github.rkosova.java_knn;

import java.util.Arrays;

/** Represents an instance of a classified data point.
 * 
 * @author Ron Kosova
 */
public class ClassPoint extends DataPoint {
    private String classification;

    /**
     * @param data Array of strings created from splitting one row of training data, class column omitted
     */
    public ClassPoint(String data[]) { 
        super(data);
        setClassification(null);
    }

    /**
     * @param data  Array of strings created from splitting one row of training data, class column omitted
     * @param classColumn  Number of the class column
     */
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
