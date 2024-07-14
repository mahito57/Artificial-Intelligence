package DecisionTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemainderCalculator {

    public static double calculateRemainder(List<Car> examples, String attribute) {
        List<String> attributeValues = getUniqueAttributeValues(examples, attribute);
        double remainder = 0.0;

        for (String value : attributeValues) {
            List<Car> subset = filterExamplesByAttributeValue(examples, attribute, value);
            double subsetWeight = (double) subset.size() / examples.size();
            double entropy = EntropyCalculator.calculateEntropy(subset, "classification");
            remainder += subsetWeight * entropy;
        }

        return remainder;
    }

    private static List<String> getUniqueAttributeValues(List<Car> examples, String attribute) {
        List<String> values = new ArrayList<>();

        for (Car car : examples) {
            String attributeValue = car.getAttributeValue(attribute);
            if (!values.contains(attributeValue)) {
                values.add(attributeValue);
            }
        }

        return values;
    }

    private static List<Car> filterExamplesByAttributeValue(List<Car> examples, String attribute, String value) {
        List<Car> subset = new ArrayList<>();

        for (Car car : examples) {
            String attributeValue = car.getAttributeValue(attribute);
            if (attributeValue.equals(value)) {
                subset.add(car);
            }
        }

        return subset;
    }

    public static void main(String[] args) {
        // Load and preprocess your car data into a List<Car>
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
        // Calculate remainder for a specific attribute, e.g., "buyingPrice"
        String attributeToCalculateRemainderFor = "buyingPrice";
        double remainder = calculateRemainder(carData, attributeToCalculateRemainderFor);
        System.out.println("Remainder for " + attributeToCalculateRemainderFor + ": " + remainder);
    }
}

