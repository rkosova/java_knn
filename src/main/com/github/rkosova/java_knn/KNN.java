package com.github.rkosova.java_knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

    public double getDistance(DataPoint dataPointA, DataPoint dataPointB){
        return 0;
    }


    // gets called by classify() and/or forecast(), where the iteration through unclassifed data is done point by point
    public void getNearestNeighbours(DataPoint unclassifiedDataPoint) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.pathToClassifiedData))) {
            String classifiedLine;
            while ((classifiedLine = br.readLine()) != null) {
               // process the line.
            }
         
        }
    }


    public void classify() throws FileNotFoundException, IOException{ 
        try (BufferedReader br = new BufferedReader(new FileReader(this.pathToUnclassifiedData))) {
            String unclassifiedLine;
            // turn to String array
            while ((unclassifiedLine = br.readLine()) != null) {
                ClassPoint unclassifiedPoint = new ClassPoint(unclassifiedLine.split(","), this.classColumn);
                getNearestNeighbours(unclassifiedPoint);
                // classify every line
            }
        }
    }


    public void forecast() throws FileNotFoundException, IOException{
        try(BufferedReader br = new BufferedReader(new FileReader(this.pathToUnclassifiedData))) {
            String unclassifiedLine;
            // turn to String array
            while ((unclassifiedLine = br.readLine()) != null) {
                NumericalPoint unclassifiedPoint = new NumericalPoint(unclassifiedLine.split(","), this.classColumn);
                getNearestNeighbours(unclassifiedPoint);
                // getNearestNeighbours
            }
        }
    }


}