package DecisionTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DecisionTree {
    private TreeNode root;
    List<Car> example;
    List<String> attr;

    public DecisionTree(List<Car> examples, List<String> attributes) {
        example = examples;
        attr = attributes;
        root = buildTree(examples, attributes);
    }

    private TreeNode buildTree(List<Car> examples, List<String> attributes) {
        // If all examples have the same classification, create a leaf node with that classification
        if (areAllExamplesSameClassification(examples)) {
            return new TreeNode(examples.get(0).getClassification());
        }

        // If there are no attributes left to split on, create a leaf node with the majority classification
        if (attributes.isEmpty()) {
            String majorityClassification = findMajorityClassification(examples);
            return new TreeNode(majorityClassification);
        }

        // Calculate the information gain for each attribute
        String bestAttribute = findBestAttribute(examples, attributes);

        // Create a new tree node with the best attribute as the decision point
        TreeNode node = new TreeNode(bestAttribute);

        // Remove the best attribute from the list of available attributes
        List<String> remainingAttributes = new ArrayList<>(attributes);
        remainingAttributes.remove(bestAttribute);

        // Split the examples based on the best attribute
        Map<String, List<Car>> attributeValueExamples = splitExamplesByAttribute(examples, bestAttribute);

        // Recursively build subtrees for each attribute value
        for (Map.Entry<String, List<Car>> entry : attributeValueExamples.entrySet()) {
            String attributeValue = entry.getKey();
            List<Car> subset = entry.getValue();
            TreeNode childNode = buildTree(subset, remainingAttributes);
            node.addChild(attributeValue, childNode);
        }

        return node;
    }

    private boolean areAllExamplesSameClassification(List<Car> examples) {
        // Check if all examples have the same classification
        String firstClassification = examples.get(0).getClassification();
        return examples.stream().allMatch(car -> car.getClassification().equals(firstClassification));
    }

    private String findMajorityClassification(List<Car> examples) {
        // Find the majority classification in a list of examples
        Map<String, Integer> classificationCounts = new HashMap<>();
        for (Car car : examples) {
            String classification = car.getClassification();
            classificationCounts.put(classification, classificationCounts.getOrDefault(classification, 0) + 1);
        }

        // Find the classification with the highest count
        String majorityClassification = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : classificationCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                majorityClassification = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return majorityClassification;
    }

    private String findBestAttribute(List<Car> examples, List<String> attributes) {
        // Find the attribute with the highest information gain
        double maxGain = -1.0;
        String bestAttribute = null;

        for (String attribute : attributes) {
            double informationGain = calculateInformationGain(examples, attribute);
            if (informationGain > maxGain) {
                maxGain = informationGain;
                bestAttribute = attribute;
            }
        }

        return bestAttribute;
    }

    private double calculateInformationGain(List<Car> examples, String attribute) {
        double entropyGoal = EntropyCalculator.calculateEntropy(examples, "classification");
        double remainder = RemainderCalculator.calculateRemainder(examples, attribute);
        return entropyGoal - remainder;
    }

    private Map<String, List<Car>> splitExamplesByAttribute(List<Car> examples, String attribute) {
        // Split examples into subsets based on the values of the chosen attribute
        Map<String, List<Car>> attributeValueExamples = new HashMap<>();

        for (Car car : examples) {
            String attributeValue = car.getAttributeValue(attribute);
            if (!attributeValueExamples.containsKey(attributeValue)) {
                attributeValueExamples.put(attributeValue, new ArrayList<>());
            }
            attributeValueExamples.get(attributeValue).add(car);
        }

        return attributeValueExamples;
    }

    public String classify(Car car) {
        return classifyRecursively(car, root);
    }

    private String classifyRecursively(Car car, TreeNode node) {
        // If the node is a leaf node, return its classification
        if (node.isLeaf()) {
//            System.out.println(node.getValue());
            return node.getValue();
        }
        // Otherwise, find the child node corresponding to the car's attribute value
        String attributeValue = car.getAttributeValue(node.getValue());
        TreeNode childNode = node.getChild(attributeValue);
        if(childNode==null){
            return parentsplurarity(car,node.getValue());
        }
        // Recursively classify using the child node
        return classifyRecursively(car, childNode);
    }

    private String parentsplurarity(Car car,String attribute) {
        boolean match;
        // Collect all examples with the specified attribute value
        List<Car> matchingExamples = new ArrayList<>();
        List<String> Attrvaluesexceptlast = new ArrayList<>(attr);
        Attrvaluesexceptlast.remove(attribute);

        // Find all examples with the same attribute value as the parent node
        for (Car example : example) {
            match = true;
            for(String e: Attrvaluesexceptlast){
                if(!example.getAttributeValue(e).equals(car.getAttributeValue(e))){
                    match = false;
                    break;
                }
            }
            if(match) matchingExamples.add(example);
        }
        // Calculate the majority classification for the specified attribute value
        return findMajorityClassification(matchingExamples);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        buildTreeString(root, stringBuilder, 0);
        return stringBuilder.toString();
    }

    private void buildTreeString(TreeNode node, StringBuilder stringBuilder, int depth) {
        if (node == null) {
            return;
        }

        // Indentation based on depth to visualize the tree structure
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("  "); // Two spaces per level
        }

        if (node.isLeaf()) {
            // Leaf node: Print the classification label
            stringBuilder.append("Class: ").append(node.getValue()).append("\n");
        } else {
            // Non-leaf node: Print the attribute and its branches
            stringBuilder.append("Attribute: ").append(node.getValue()).append("\n");

            // Recursively build branches
            for (Map.Entry<String, TreeNode> entry : node.getChildren().entrySet()) {
                String attributeValue = entry.getKey();
                TreeNode childNode = entry.getValue();

                // Print the branch's attribute value
                for (int i = 0; i <= depth; i++) {
                    stringBuilder.append("  "); // Two spaces per level
                }
                stringBuilder.append("Value: ").append(attributeValue).append("\n");

                // Recursively build the subtree
                buildTreeString(childNode, stringBuilder, depth + 1);
            }
        }
    }

    public static void main(String[] args) {
//        // Load and preprocess your car data into a List<Car>
//
//        // Define the list of attributes
//        List<String> attributes = List.of("buyingPrice", "maintenancePrice", "numberOfDoors", "numberOfPersons", "luggageBoot", "safety");
//
//        // Create a decision tree
//        DecisionTree decisionTree = new DecisionTree(carData, attributes);
//
//        System.out.println(decisionTree);
//        // Classify a car using the decision tree
//        Car carToClassify = new Car("vhigh", "vhigh", "2", "2", "small", "high", "unacc");
//        String classification = decisionTree.classify(carToClassify);
//        System.out.println("Classification: " + classification);
    }
}

