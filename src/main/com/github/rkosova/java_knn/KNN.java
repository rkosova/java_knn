package com.github.rkosova.java_knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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

    public double getDistance(DataPoint dataPointA, DataPoint dataPointB) {
        double distance = 0D;


        for(int i = 0; i < dataPointA.getX().size(); i++) {
            distance += Math.pow(dataPointA.getX().get(i) - dataPointB.getX().get(i), 2);
        }
        return Math.sqrt(distance);
    }


    // gets called by classify() and/or forecast(), where the iteration through unclassifed data is done point by point
    // void for now, must return k nearest neighbours
    public ArrayList<DataPoint> getNearestNeighbours(DataPoint unclassifiedDataPoint, char type) throws FileNotFoundException, IOException {
        String classifiedLine = ""; 
        DataPoint classifiedPoint = null;
        ArrayList<DataPoint> distancedPoints = new ArrayList<>(); 

        try (BufferedReader br = new BufferedReader(new FileReader(this.pathToClassifiedData))) {

            while ((classifiedLine = br.readLine()) != null) {

                if (type == 'F') {
                    classifiedPoint = new NumericalPoint(classifiedLine.split(","), this.classColumn);
                } else if (type == 'C') {
                    classifiedPoint = new ClassPoint(classifiedLine.split(","), this.classColumn); 
                } 

                classifiedPoint.setDistance(getDistance(classifiedPoint, unclassifiedDataPoint));
                distancedPoints.add(classifiedPoint);
                  
            }
         
        }

        
        Collections.sort(distancedPoints, new Comparator<DataPoint>(){
            public int compare(DataPoint o1, DataPoint o2)
            {
                return Double.compare(o1.getDistance(), o2.getDistance());
            }
        });

        return distancedPoints;
    }


    public void classify() throws FileNotFoundException, IOException{ 
        try (BufferedReader br = new BufferedReader(new FileReader(this.pathToUnclassifiedData))) {
            String unclassifiedLine;
            ArrayList<DataPoint> distancedDataPoints;
            // turn to String array
            while ((unclassifiedLine = br.readLine()) != null) {
                ClassPoint unclassifiedPoint = new ClassPoint(unclassifiedLine.split(","));
                distancedDataPoints = getNearestNeighbours(unclassifiedPoint, 'C');
                
                for (int i = 0; i < distancedDataPoints.size(); i++) {
                    System.out.printf("Point %f, %f is %f away. \n"
                                    , unclassifiedPoint.getX().get(0)
                                    , unclassifiedPoint.getX().get(1)
                                    , distancedDataPoints.get(i).getDistance());
                }
            }
        }
    }


    public void forecast() throws FileNotFoundException, IOException{
        try(BufferedReader br = new BufferedReader(new FileReader(this.pathToUnclassifiedData))) {
            String unclassifiedLine;
            ArrayList<DataPoint> distancedDataPoints;
            // turn to String array
            while ((unclassifiedLine = br.readLine()) != null) {
                NumericalPoint unclassifiedPoint = new NumericalPoint(unclassifiedLine.split(","), this.classColumn);
                distancedDataPoints = getNearestNeighbours(unclassifiedPoint, 'F');

                for (int i = 0; i < distancedDataPoints.size(); i++) {
                    System.out.println(distancedDataPoints.get(i).getDistance());
                }
            }
        }
    }


}