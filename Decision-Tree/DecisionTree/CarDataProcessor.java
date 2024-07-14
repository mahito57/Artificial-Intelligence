package DecisionTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CarDataProcessor {

    public static void main(String[] args) {
        List<Car> carList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("car evaluation dataset/car.data"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 7) {
                    // Assuming the order of attributes in the file is buying, maintenance, doors, persons, luggageBoot, safety, classification
                    String buyingPrice = values[0];
                    String maintenancePrice = values[1];
                    String numberOfDoors = values[2];
                    String numberOfPersons = values[3];
                    String luggageBoot = values[4];
                    String safety = values[5];
                    String classification = values[6];

                    Car car = new Car(buyingPrice, maintenancePrice, numberOfDoors, numberOfPersons, luggageBoot, safety, classification);
                    carList.add(car);
                } else {
                    System.err.println("Skipping invalid data: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now you have an ArrayList of Car objects
//        for (Car car : carList) {
//            System.out.println(car);
//        }
        int numIterations = 20;
        List<Double> accuracyList = new ArrayList<>();

        for (int i = 0; i < numIterations; i++) {
            // Split the data into training (80%) and testing (20%) randomly
            List<Car> shuffledData = new ArrayList<>(carList);
            Collections.shuffle(shuffledData, new Random());

            int splitIndex = (int) (shuffledData.size() * .8);
            List<Car> trainingData = shuffledData.subList(0, splitIndex);
            List<Car> testData = shuffledData.subList(splitIndex, shuffledData.size());

            // Create and build the decision tree using your DecisionTree class
            // Define the list of attributes
            List<String> attributes = List.of("buyingPrice", "maintenancePrice", "numberOfDoors", "numberOfPersons", "luggageBoot", "safety");
            // Create a decision tree
            DecisionTree decisionTree = new DecisionTree(trainingData, attributes);
            //System.out.println(decisionTree);
            // Evaluate the decision tree on the test data and calculate accuracy
            int correctPredictions = 0;
            for (Car car : testData) {
                String predictedClassification = decisionTree.classify(car);
                if (predictedClassification != null && predictedClassification.equals(car.getClassification())) {
                    correctPredictions++;
                }
            }

            double accuracy = (double) correctPredictions / testData.size();
            accuracyList.add(accuracy);
        }

        // Calculate the mean and standard deviation of accuracy values
        double sum = 0;
        for (Double accuracy : accuracyList) {
           // System.out.println(accuracy);
            sum += accuracy;
        }
        double meanAccuracy = sum / numIterations;

        double sumOfSquaredDifferences = 0;
        for (Double accuracy : accuracyList) {
            double diff = accuracy - meanAccuracy;
            sumOfSquaredDifferences += diff * diff;
        }
        double standardDeviation = Math.sqrt(sumOfSquaredDifferences / numIterations);

        // Print the mean accuracy and standard deviation
        System.out.println("Mean Accuracy: " + meanAccuracy);
        System.out.println("Standard Deviation of Accuracy: " + standardDeviation);
    }
}

