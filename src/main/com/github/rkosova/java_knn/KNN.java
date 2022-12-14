package com.github.rkosova.java_knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;




/** Represents an instance of the kNN model.
 * 
 * @author Ron Kosova
 */
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

    /**
     * 
     * @param pathToClassifiedData Path to the training data
     * @param pathToUnclassifiedData Path to the unclassified data
     * @param pathToWrite Path to where the output is written
     * @param classColumn Number of the class column for the training data
     */
    public KNN(String pathToClassifiedData, String pathToUnclassifiedData, String pathToWrite, int classColumn) {
        this(pathToClassifiedData, pathToUnclassifiedData, pathToWrite, classColumn, 3);
    }

    /**
     * 
     * @param pathToClassifiedData Path to the training data
     * @param pathToUnclassifiedData Path to the unclassified data
     * @param pathToWrite Path to where the output is written
     * @param classColumn Number of the class column for the training data
     * @param k k value
     */
    public KNN(String pathToClassifiedData, String pathToUnclassifiedData, String pathToWrite, int classColumn, int k) {
        this.pathToClassifiedData = pathToClassifiedData;
        this.pathToUnclassifiedData = pathToUnclassifiedData;
        this.pathToWrite = pathToWrite;
        this.classColumn = classColumn;
        this.k = k;
    }

    enum Model {
        FORECAST,
        CLASSIFY
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
    
    /** Gets the distance between two points
     * 
     * @param dataPointA 
     * @param dataPointB
     * @return Distance between @param dataPointA and @param dataPointB
     */
    public double getDistance(DataPoint dataPointA, DataPoint dataPointB) {
        double distance = 0D;

        for(int i = 0; i < dataPointA.getX().size(); i++) {
            distance += Math.pow(dataPointA.getX().get(i) - dataPointB.getX().get(i), 2);
        }
        return Math.sqrt(distance);
    }

    /** Returns a k-sized sorted ArrayList of DataPoint objects from closest to furthest.
     * 
     * @param unclassifiedDataPoint DataPoint object from unclassified dataset for which the neighbours must be found
     * @param type  Type of model to be used (FORECAST or CLASSIFY)
     * @return  Sorted, k-sized ArrayList of DataPoints that represents the k Nearest Neighbours of the @param unclassifiedDataPoint parameter
     * @throws IOException
     */
    public ArrayList<DataPoint> getNearestNeighbours(DataPoint unclassifiedDataPoint, Model type) throws IOException {
        String classifiedLine = ""; 
        DataPoint classifiedPoint = null;
        ArrayList<DataPoint> distancedPoints = new ArrayList<>(); 
        ArrayList<DataPoint> kNearestNeighbours = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(this.pathToClassifiedData));

        while ((classifiedLine = br.readLine()) != null) {

            if (type == Model.FORECAST) {
                classifiedPoint = new NumericalPoint(classifiedLine.split(","), this.classColumn);
            } else if (type == Model.CLASSIFY) {
                classifiedPoint = new ClassPoint(classifiedLine.split(","), this.classColumn); 
            } 

            classifiedPoint.setDistance(getDistance(classifiedPoint, unclassifiedDataPoint));
            distancedPoints.add(classifiedPoint);
        }
        br.close();

        Collections.sort(distancedPoints, new Comparator<DataPoint>(){
            public int compare(DataPoint o1, DataPoint o2)
            {
                return Double.compare(o1.getDistance(), o2.getDistance());
            }
        });

        for (int i = 0; i < k; i++) {
            kNearestNeighbours.add(distancedPoints.get(i));
        }

        return kNearestNeighbours; 
    }


    /** Classifies discrete class unclassified data points from k nearest neighbours and writes to pathToFile.
     * 
     * @throws IOException
     */
    public void classify() throws IOException{ 
        BufferedReader br = new BufferedReader(new FileReader(this.pathToUnclassifiedData));
        String unclassifiedLine;
        ArrayList<DataPoint> kNearestNeighbours;
        File writeFile = new File(this.pathToWrite);
        FileWriter writer = new FileWriter(writeFile);

        while ((unclassifiedLine = br.readLine()) != null) {
            ClassPoint unclassifiedPoint = new ClassPoint(unclassifiedLine.split(","));
            kNearestNeighbours = getNearestNeighbours(unclassifiedPoint, Model.CLASSIFY);

            ArrayList<String>  classOccuranceClass = new ArrayList<>();
            ArrayList<Integer> classOccuranceOccurance = new ArrayList<>();
            String mostOccuringClass = null;
            int classOccuranceInt = 0; 
            
            if (this.k > 2) { 
                for (DataPoint c : kNearestNeighbours) {
                    if ((classOccuranceClass.size() != 0 && classOccuranceOccurance.size() != 0) || !classOccuranceClass.contains(((ClassPoint) c).getClassification())) {
                        classOccuranceClass.add(((ClassPoint) c).getClassification());
                        classOccuranceOccurance.add(1);
                    } else if (classOccuranceClass.contains(((ClassPoint) c).getClassification())) {
                        classOccuranceOccurance.add(classOccuranceOccurance.get(classOccuranceClass.indexOf(((ClassPoint) c).getClassification())) + 1);
                    }
                }

                for (String cl : classOccuranceClass) {
                    if (mostOccuringClass == null && classOccuranceInt == 0) {
                        mostOccuringClass = cl;
                        classOccuranceInt = classOccuranceOccurance.get(classOccuranceClass.indexOf(cl));
                    } else if (classOccuranceOccurance.get(classOccuranceClass.indexOf(cl)) > classOccuranceInt) {
                        mostOccuringClass = cl;
                        classOccuranceInt = classOccuranceOccurance.get(classOccuranceClass.indexOf(cl));
                    }
                }

                unclassifiedPoint.setClassification(mostOccuringClass);
                
            } else {
                unclassifiedPoint.setClassification(((ClassPoint) kNearestNeighbours.get(0)).getClassification());
            }

            writer.write(unclassifiedPoint.getClassification().trim() + "\n");

        }
        writer.close();
        br.close();
    }
    
    /** Forecasts numerical data points from k nearest neighbours and writes to pathToWrite.
     * 
     * @throws IOException
     */
    public void forecast() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(this.pathToUnclassifiedData));
        String unclassifiedLine;
        ArrayList<DataPoint> distancedDataPoints; 
        File writeFile = new File(this.pathToWrite);
        FileWriter writer = new FileWriter(writeFile);

        while ((unclassifiedLine = br.readLine()) != null) {
            NumericalPoint unclassifiedPoint = new NumericalPoint(unclassifiedLine.split(","));
            distancedDataPoints = getNearestNeighbours(unclassifiedPoint, Model.FORECAST);

            double sum = 0d;

            for (DataPoint n : distancedDataPoints) {
                sum += ((NumericalPoint) n).getKnown();
            }

            unclassifiedPoint.setKnown(sum / distancedDataPoints.size());
            writer.write(Double.toString(unclassifiedPoint.getKnown()) + "\n");
        }
        writer.close();
        br.close();
    }

    /** Gets RMSE of two files containing the testing data observation column and the model output
     * 
     * @param pathToObserved  Path to file containting observed data y column
     * @param pathToPredicted  Path to file containting model output
     * @return
     * @throws IOException
     */
    public static double getRMSE(String pathToObserved, String pathToPredicted) throws IOException{
        double rmse = 0;
        double distanceSum = 0;
        int lineCount = 0;

        BufferedReader observedReader = new BufferedReader(new FileReader(pathToObserved));
        BufferedReader predictedReader = new BufferedReader(new FileReader(pathToPredicted));
    
        while (true) {
            String lineObserved = observedReader.readLine();
            String linePredicted = predictedReader.readLine();

            if (lineObserved == null || linePredicted == null) {
                break;
            }

            double observation = Double.parseDouble(lineObserved);
            double prediction = Double.parseDouble(linePredicted);

            distanceSum += Math.pow(observation - prediction, 2);
            lineCount++;
        }

        observedReader.close();
        predictedReader.close();

        rmse = Math.sqrt(distanceSum/lineCount);

        return rmse;
    }

    /**Gets the accuracy between two files, one containing the training data class column and one the model output
     * 
     * @param pathToObserved  Path to trainging data class column
     * @param pathToPredicted  Path to model output
     * @return
     * @throws IOException
     */
    public static double getAccuracy(String pathToObserved, String pathToPredicted) throws IOException {
        double accuracy = 0;
        int lineCount = 0;

        BufferedReader observedReader = new BufferedReader(new FileReader(pathToObserved));
        BufferedReader predictedReader = new BufferedReader(new FileReader(pathToPredicted));

        while (true) {
            String classObserved = observedReader.readLine();
            String classPredicted = predictedReader.readLine();

            if (classObserved == null || classPredicted == null) {
                break;
            }

            if (classObserved.equals(classPredicted)) {
                accuracy++;
            }
            
            lineCount++;
        }

        observedReader.close();
        predictedReader.close();

        accuracy /= lineCount;

        return accuracy;
    }
} // maybe integrate accuracy data into classify() or forecast()