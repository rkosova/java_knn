package com.github.rkosova.java_knn;


public class KNN {

    private String pathToClassifiedData;
    private String pathToUnclassifiedData;
    private String pathToWrite;

    private int classColumn;
    private int k;

    public KNN()
    {
        this(null, null, null, 0);
    }

    public KNN(String pathToClassifiedData, String pathToUnclassifiedData, String pathToWrite, int classColumn) {
        this(pathToClassifiedData, pathToUnclassifiedData, pathToWrite, classColumn, 3);
    }

    public KNN(String pathToClassifiedData, String pathToUnclassifiedData, String pathToWrite, int classColumn, int k) {
        this.pathToClassifiedData = pathToClassifiedData;
        this.pathToUnclassifiedData = pathToUnclassifiedData;
        this.pathToWrite = pathToWrite;
        this.classColumn = classColumn;
        this.k = k;
    }

    
    public void setPathToClassifiedData(String pathToClassifiedData) {
        this.pathToClassifiedData = pathToClassifiedData;
    }

    public String getPathToClassifiedData() {
        return pathToClassifiedData;
    }

    public void setPathToUnclassifiedData(String pathToUnclassifiedData) {
        this.pathToUnclassifiedData = pathToUnclassifiedData;
    }

    public String getPathToUnclassifiedData() {
        return pathToUnclassifiedData;
    }

    public void setPathToWrite(String pathToWrite) {
        this.pathToWrite = pathToWrite;
    }

    public String getPathToWrite() {
        return pathToWrite;
    }

    public void setClassColumn(int classColumn) {
        this.classColumn = classColumn;
    }

    public int getClassColumn() {
        return classColumn;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getK() {
        return k;
    }







    public static void main(String[] args) {
        
    }
}