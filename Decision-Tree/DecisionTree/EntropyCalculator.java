package DecisionTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class EntropyCalculator {

    public static double calculateEntropy(List<Car> examples, String attribute) {
        // Create a map to count the occurrences of each unique value of the attribute
        Map<String, Integer> valueCounts = new HashMap<>();

        // Count the occurrences of each unique value of the attribute in the examples
        for (Car car : examples) {
            String attributeValue = car.getAttributeValue(attribute);
            valueCounts.put(attributeValue, valueCounts.getOrDefault(attributeValue, 0) + 1);
        }

        int totalExamples = examples.size();
        double entropy = 0.0;

        // Calculate the entropy for each unique attribute value and weight it
        for (String attributeValue : valueCounts.keySet()) {
            int count = valueCounts.get(attributeValue);
            double probability = (double) count / totalExamples;
            entropy -= probability * log2(probability);
        }

        return entropy;
    }

    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public static void main(String[] args) {
        // Load and preprocess your car data into a List<Car>

        // Calculate entropy for a specific attribute, e.g., "buyingPrice"
        List<Car> carData = new ArrayList<>();

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
                    carData.add(car);
                } else {
                    System.err.println("Skipping invalid data: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String attributeToCalculateEntropyFor = "buyingPrice";
        double entropy = calculateEntropy(carData, attributeToCalculateEntropyFor);
        System.out.println("Entropy for " + attributeToCalculateEntropyFor + ": " + entropy);
    }
}

