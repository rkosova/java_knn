package com.github.rkosova.java_knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;




// ADD DOCU COMMENTS

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

    public double getDistance(DataPoint dataPointA, DataPoint dataPointB) {
        double distance = 0D;


        for(int i = 0; i < dataPointA.getX().size(); i++) {
            distance += Math.pow(dataPointA.getX().get(i) - dataPointB.getX().get(i), 2);
        }
        return Math.sqrt(distance);
    }


    public ArrayList<DataPoint> getNearestNeighbours(DataPoint unclassifiedDataPoint, Model type) throws FileNotFoundException, IOException {
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